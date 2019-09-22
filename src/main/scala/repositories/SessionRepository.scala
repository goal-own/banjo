package repositories

import doobie.util.transactor.Transactor
import models.SessionModel._

class SessionRepository[F[_]](transactor: Transactor[F])
    extends Repository[F, Session, Token, Id] {

  /*
    How to initialize, that it has side effected:
    1) Number of affected rows
    2) Object
   */
  override def findAll: F[List[Session]] = ???

  override def findById(id: Id): F[Option[Session]] = ???

  override def findByParam(param: Token): F[Option[Session]] = ???

  override def persist(e: Session): F[Unit] = ???

  override def delete(e: Session): F[Unit] = ???
}
