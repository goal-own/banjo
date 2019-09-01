package services

import cats.MonadError
import cats.syntax.all._
import models.{AlreadyExistsException, NotExistsException, Person}
import repositories.InMemoryRepository

class PersonService[F[_]](private val repo: InMemoryRepository[F])(
  implicit F: MonadError[F, Throwable]
) extends Service[F, Person] {

  override def findAll: F[List[Person]] = repo.findAll
  override def find(e: Person): F[Option[Person]] = repo.find(e)
  override def persist(e: Person): F[Unit] =
    repo.find(e).flatMap { maybe =>
      maybe.fold(repo.persist(e))(
        _ => F.raiseError(AlreadyExistsException(s"person: $e already exists"))
      )
    }
  override def delete(e: Person): F[Unit] =
    repo
      .find(e)
      .flatMap(
        person =>
          person.fold(
            F.raiseError(NotExistsException(s"person: $e not exists")) >> F.unit
          )(x => repo.delete(x))
      )
}

object PersonService {
  def apply[F[_]](repo: InMemoryRepository[F])(
    implicit F: MonadError[F, Throwable]
  ): PersonService[F] = new PersonService(repo)
}
