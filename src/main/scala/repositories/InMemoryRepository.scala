package repositories

import cats.syntax.functor._
import cats.effect.Sync
import models.{PersonId, TestPerson, Username}

/*
Used in test-cases instead of db
 */
class InMemoryRepository[F[_]](implicit F: Sync[F])
    extends Repository[F, TestPerson, Username, PersonId] {
  private val store =
    scala.collection.mutable.Buffer[TestPerson]()

  override def findAll: F[List[TestPerson]] = F.delay(store.toList)
  override def findById(id: PersonId): F[Option[TestPerson]] =
    F.delay(store.find(_.id == id))
  override def findByParam(param: Username): F[Option[TestPerson]] =
    F.delay(store.find(_.username == param))
  override def persist(e: TestPerson): F[Int] = F.delay(store += e).map(_ => 1)
  override def delete(e: TestPerson): F[Int] = F.delay(store -= e).map(_ => 1)
  override def update(e: TestPerson): F[Int] = ??? // TODO
}
