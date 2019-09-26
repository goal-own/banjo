import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import org.log4s.Logger

package object services {

  /**
    * Base trait for services
    */
  trait Service[F[_], E, P, I] {
    type R[T] = Either[Throwable, T]

    def update(e: E): F[R[E]]
    def findById(id: I): F[Option[E]]
    def findByParam(param: P): F[Option[E]]
    def findAll: F[List[E]]
    def persist(e: E): F[R[E]]
    def delete(e: E): F[R[E]]
  }

  /**
  Base errors for services layer
    */
  case class AlreadyExistsException(info: String) extends Exception(info)
  case class DbException(info: String = "unknown db exception")
      extends Exception(info)
  case class NotExistsException(info: String) extends Exception(info)

  def affect[T, F[_]: Sync](t: T, action: F[Int])(
    implicit logger: Logger
  ): F[Either[Throwable, T]] =
    action.flatMap[Either[Throwable, T]] {
      case affected @ 1 =>
        Sync[F]
          .delay {
            logger.info(s"persisted: $t, affected rows: $affected")
          }
          .as(Right(t))
      case _ =>
        Sync[F].delay {
          Left(DbException())
        }
    }
}
