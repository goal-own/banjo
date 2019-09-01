package repositories

import cats.effect.Sync
import models.{Id, User, Username}

/*
Used in test-cases instead of db
 */
class InMemoryRepository[F[_]](implicit F: Sync[F])
    extends Repository[F, User, Username, Id] {
  private val store =
    scala.collection.mutable.Buffer[User]()

  override def findAll: F[List[User]] = F.delay(store.toList)
  override def findById(id: Id): F[Option[User]] =
    F.delay(store.find(_.id == id))
  override def findByParam(param: Username): F[Option[User]] =
    F.delay(store.find(_.username == param))
  override def persist(e: User): F[Unit] = F.delay(store += e)
  override def delete(e: User): F[Unit] = F.delay(store -= e)
}
