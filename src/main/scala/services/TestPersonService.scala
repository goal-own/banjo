package services

import cats.MonadError
import cats.syntax.flatMap._
import cats.syntax.functor._
import models.{PersonId, TestPerson, Username}
import repositories.InMemoryRepository

class TestPersonService[F[_]](private val repo: InMemoryRepository[F])(
  implicit F: MonadError[F, Throwable]
) extends Service[F, TestPerson, Username, PersonId] {

  override def findAll: F[List[TestPerson]] = repo.findAll
  override def findByParam(e: Username): F[Option[TestPerson]] =
    repo.findByParam(e)
  override def persist(e: TestPerson): F[R[TestPerson]] = ??? // TODO
//    repo.findById(e.id).flatMap { maybe =>
//      maybe.fold(repo.persist(e))(
//        _ => F.raiseError(AlreadyExistsException(s"user: $e already exists"))
//      ) >> F.unit
//    }
  override def delete(e: TestPerson): F[R[TestPerson]] = ??? // TODO
//    repo
//      .findById(e.id)
//      .flatMap(
//        person =>
//          person.fold(
//            F.raiseError(NotExistsException(s"person: $e not exists")) >> F.unit
//          )(x => repo.delete(x))
//      )
  override def findById(id: PersonId): F[Option[TestPerson]] = repo.findById(id)

  override def update(e: TestPerson): F[R[TestPerson]] = ??? // TODO
}

object TestPersonService {
  def apply[F[_]](repo: InMemoryRepository[F])(
    implicit F: MonadError[F, Throwable]
  ): TestPersonService[F] = new TestPersonService(repo)
}
