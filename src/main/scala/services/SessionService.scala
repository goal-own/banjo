package services

import cats.effect.Sync
import cats.syntax.flatMap._
import models.SessionModel.{Session, SessionId, Token, UserId}
import repositories.SessionRepository
import org.log4s.Logger

class SessionService[F[_]: Sync](private val repo: SessionRepository[F])(
  implicit logger: Logger
) extends Service[F, Session, Token, UserId] {

  override def findById(id: UserId): F[Option[Session]] =
    repo.findById(id)

  override def findByParam(param: Token): F[Option[Session]] =
    repo.findByParam(param)

  override def findAll: F[List[Session]] = repo.findAll

  override def persist(e: Session): F[R[Session]] =
    repo
      .findById(e.userId)
      .flatMap {
        _.fold(affect(e, repo.persist(e)))(
          _ =>
            Sync[F].delay(
              Left(AlreadyExistsException(s"session: $e already exists"))
          )
        )
      }

  override def delete(e: Session): F[R[Session]] =
    repo
      .findById(e.userId)
      .flatMap {
        case Some(session) =>
          affect(session, repo.delete(session))
        case None =>
          Sync[F].delay(Left(NotExistsException(s"session: $e not exists")))
      }

  override def update(e: Session): F[R[Session]] =
    repo.findById(e.userId).flatMap {
      case Some(_) => affect(e, repo.update(e))
      case None =>
        Sync[F].delay(Left(NotExistsException(s"session: $e not exists")))
    }

  def validateSession(session: SessionId): F[Option[SessionId]] = {
    repo.validate(session)
  }
}
