import cats.data.Kleisli
import cats.effect.{
  Blocker,
  ConcurrentEffect,
  ContextShift,
  Effect,
  ExitCode,
  IO,
  IOApp,
  Sync,
  Timer
}
import endpoints.EndpointsRouter
import cats.implicits.toFunctorOps
import client.ClientTest
import org.http4s.{Request, Response}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import org.log4s.{Logger, getLogger}
import config.{Config, ServerConfig}
import database.Database
import doobie.util.transactor.Transactor
import utils.StreamUtils._
import fs2._
import mock.InitFriends
import org.http4s.client.blaze.BlazeClientBuilder
import repositories.ImageStoreRepository

import scala.concurrent.ExecutionContext

object Banjo extends IOApp {
  implicit val logger: Logger = getLogger(getClass.getName)

  def httpApp[F[_]: Effect: ContextShift](
    serverConfig: ServerConfig,
    blocker: Blocker,
    tr: Transactor[F]
  ): Kleisli[F, Request[F], Response[F]] =
    Router(
      "/test" -> EndpointsRouter.testEndpoint,
      "/session" -> EndpointsRouter.sessionEndpoint(tr),
      "/media" -> EndpointsRouter.storeStoriesEndpoint(
        blocker,
        serverConfig,
        tr
      )
    ).orNotFound

  def stream[F[_]: Sync: ContextShift](implicit E: ConcurrentEffect[F],
                                       T: Timer[F]): Stream[F, Unit] =
    for {
      cfg <- eval(Config.load[F])
      _ <- evalF(logger.info("Config loaded successfully"))
      (port, host) = cfg.server.port -> cfg.server.host
      blocker <- Stream.resource(Blocker[F])
      transactor <- evalF(Database.transactor(cfg.database, blocker))
      client <- Stream.resource(
        BlazeClientBuilder[F](ExecutionContext.global).resource
      )
      _ <- Stream.emit(new ClientTest[F](client).makeRequest)
      _ <- eval(
        new InitFriends[F](new ImageStoreRepository[F](transactor)).init
      )
      // test
      _ <- BlazeServerBuilder[F]
        .bindHttp(port, host)
        .withHttpApp(httpApp[F](cfg.server, blocker, transactor))
        .serve
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    stream[IO].compile.drain.as(ExitCode.Success)
}
