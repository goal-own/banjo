import cats.data.Kleisli
import cats.effect.{ConcurrentEffect, ExitCode, IO, IOApp, Sync, Timer}
import endpoints.EndpointsRouter
import cats.implicits.toFunctorOps
import org.http4s.{Request, Response}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import org.log4s.{Logger, getLogger}
import config.Config
import utils.StreamUtils._
import fs2._

object Banjo extends IOApp {
  implicit val logger: Logger = getLogger(getClass.getName)

  def httpApp[F[_]: Sync]: Kleisli[F, Request[F], Response[F]] =
    Router(
      "/test" -> EndpointsRouter.testEndpoint,
      "/session" -> EndpointsRouter.testEndpoint
    ).orNotFound

  def stream[F[_]: Sync](implicit E: ConcurrentEffect[F],
                         T: Timer[F]): Stream[F, Unit] =
    for {
      cfg <- eval(Config.load[F])
      _ <- evalF(logger.info("Config loaded successfully"))
      (port, host) = cfg.server.port -> cfg.server.host
      _ <- BlazeServerBuilder[F]
        .bindHttp(port, host)
        .withHttpApp(httpApp[F])
        .serve
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    stream[IO].compile.drain.as(ExitCode.Success)
}
