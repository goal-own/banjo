package services

import cats.MonadError
import cats.syntax.all._
import models.{AlreadyExistsException, Id, NotExistsException, User, Username}
import repositories.InMemoryRepository

class UserService[F[_]](private val repo: InMemoryRepository[F])(
  implicit F: MonadError[F, Throwable]
) extends Service[F, User, Username, Id] {

  override def findAll: F[List[User]] = repo.findAll
  override def findByParam(e: Username): F[Option[User]] = repo.findByParam(e)
  override def persist(e: User): F[Unit] =
    repo.findById(e.id).flatMap { maybe =>
      maybe.fold(repo.persist(e))(
        _ => F.raiseError(AlreadyExistsException(s"user: $e already exists"))
      )
    }
  override def delete(e: User): F[Unit] =
    repo
      .findById(e.id)
      .flatMap(
        person =>
          person.fold(
            F.raiseError(NotExistsException(s"person: $e not exists")) >> F.unit
          )(x => repo.delete(x))
      )
  override def findById(id: Id): F[Option[User]] = repo.findById(id)
}

object UserService {
  def apply[F[_]](repo: InMemoryRepository[F])(
    implicit F: MonadError[F, Throwable]
  ): UserService[F] = new UserService(repo)
}
