package services

import cats.effect.{ContextShift, Sync, Timer}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import repositories.Repository

case class Data()
case class BanjoService[F[_]: Sync](repository: Repository[Data])(
  implicit val cs: ContextShift[F],
  implicit val timer: Timer[F],
) extends Http4sDsl[F] {
  val service = HttpRoutes
    .of[F] {
      case GET -> Root / "hello" / name =>
        Ok(s"demo, $name")
    }
}
