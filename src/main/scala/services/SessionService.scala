package services

import cats.MonadError
import cats.effect.Sync
import cats.syntax.flatMap._
import models.SessionModel.{Session, Token, UserId}
import repositories.SessionRepository
import models.{AlreadyExistsException, NotExistsException}
import org.log4s.Logger

class SessionService[F[_]](private val repo: SessionRepository[F])(
  implicit F: MonadError[F, Throwable],
  S: Sync[F],
  logger: Logger
) extends Service[F, Session, Token, UserId] {

  override def findById(id: UserId): F[Option[Session]] =
    repo.findById(id)

  override def findByParam(param: Token): F[Option[Session]] =
    repo.findByParam(param)

  override def findAll: F[List[Session]] =
    repo.findAll

  override def persist(e: Session): F[Unit] =
    repo.findById(e.userId).flatMap {
      _.fold(
        repo
          .persist(e)
          .flatMap(
            affected =>
              S.delay(logger.info(s"persisted: $e, affected rows: $affected"))
          )
      )(
        _ => F.raiseError(AlreadyExistsException(s"session: $e already exists"))
      )
    }

  override def delete(e: Session): F[Unit] =
    repo
      .findById(e.userId)
      .flatMap(
        _.fold(
          F.raiseError(NotExistsException(s"session: $e not exists")) >> F.unit
        )(
          x =>
            repo
              .delete(x)
              .flatMap(
                affected =>
                  S.delay(
                    logger
                      .info(s"session: $e deleted, affected rows: $affected")
                )
            )
        )
      )

  override def update(e: Session): F[Unit] =
    repo.findById(e.userId).flatMap { maybe =>
      maybe.fold(
        F.raiseError(NotExistsException(s"session: $e not exists")) >> F.unit
      )(
        _ =>
          repo
            .update(e)
            .flatMap(
              affected =>
                S.delay(
                  logger.info(s"session: $e updated, affected rows: $affected")
              )
          )
      )
    }
}
