package repositories

import cats.effect.Sync
import models.{Id, TestPerson, Username}

/*
Used in test-cases instead of db
 */
class InMemoryRepository[F[_]](implicit F: Sync[F])
    extends Repository[F, TestPerson, Username, Id] {
  private val store =
    scala.collection.mutable.Buffer[TestPerson]()

  override def findAll: F[List[TestPerson]] = F.delay(store.toList)
  override def findById(id: Id): F[Option[TestPerson]] =
    F.delay(store.find(_.id == id))
  override def findByParam(param: Username): F[Option[TestPerson]] =
    F.delay(store.find(_.username == param))
  override def persist(e: TestPerson): F[Unit] = F.delay(store += e)
  override def delete(e: TestPerson): F[Unit] = F.delay(store -= e)
}
