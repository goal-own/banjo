package repositories

import cats.effect.Sync
import models.Person

/*
Used in test-cases instead of db
 */
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
