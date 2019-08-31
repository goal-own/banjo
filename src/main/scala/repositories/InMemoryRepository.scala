package repositories

import cats.effect.Sync

/*
Used in test-cases instead of db
 */
case class Person(id: Int, name: String)
case class AlreadyExistsException(info: String) extends Exception(info)
case class NotExistsException(info: String) extends Exception(info)

class InMemoryRepository[F[_]](implicit F: Sync[F])
    extends Repository[F, Person] {

  private val store =
    scala.collection.mutable.Buffer[Person]()

  override def findAll: F[List[Person]] = F.delay(store.toList)
  override def find(e: Person): F[Option[Person]] =
    F.delay(store.find(_ == e))
  override def persist(e: Person): F[Unit] = F.delay(store += e)
  override def delete(e: Person): F[Unit] = F.delay(store -= e)
}
