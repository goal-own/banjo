package repositories

import cats.effect.Effect
import doobie.util.transactor.Transactor
import models.SessionModel._
import doobie.syntax._
import doobie.implicits._

class SessionRepository[F[_]: Effect](transactor: Transactor[F])
    extends Repository[F, Session, Token, UserId] {

  override def findAll: F[List[Session]] =
    sql"SELECT * FROM public.session"
      .query[Session]
      .to[List]
      .transact(transactor)

  override def findById(id: UserId): F[Option[Session]] =
    sql"SELECT * FROM public.session WHERE session.user_id = ${id.id}"
      .query[Session]
      .option
      .transact(transactor)

  override def findByParam(param: Token): F[Option[Session]] =
    sql"SELECT * FROM public.session WHERE session.token = ${param.accessToken}"
      .query[Session]
      .option
      .transact(transactor)

  override def persist(e: Session): F[Int] =
    sql"INSERT into public.session (user_id, token, session_id) VALUES (${e.userId.id} , ${e.token.accessToken}, ${e.sessionId.id})".update.run
      .transact(transactor)

  override def delete(e: Session): F[Int] =
    sql"DELETE FROM public.session WHERE user_id = ${e.userId}".update.run
      .transact(transactor)

  override def update(e: Session): F[Int] =
    sql"UPDATE public.session SET session_id = ${e.sessionId}, token = ${e.token}, user_id = ${e.userId}".update.run
      .transact(transactor)
}
