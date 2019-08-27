package repositories

trait Repository[T] {
  def update(t: T): Option[R[T]]
  def add(t: T): R[T]
  def delete(t: T): Option[R[T]]
}

/*
Test repository. Used instead of db first
 */

final class TestRepository[T](private val data: List[T]) extends Repository[T] {
  def update(t: T): Option[R[T]] =
    if (data.contains(t))
      Some(new TestRepository(data.updated(data.indexOf(t), t)))
    else None

  def add(t: T): R[T] = new TestRepository(t :: data)

  def delete(t: T): Option[R[T]] = // bug. Only in test purpose where all T's are different
    if (data.contains(t)) Some(new TestRepository(data.filterNot(_ == t)))
    else None
}

object TestRepository {
  def apply[T](data: T*): R[T] = new TestRepository(data.toList)
}
