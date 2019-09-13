import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp, Sync}
import endpoints.PersonHttpEndpoint
import cats.implicits._
import org.http4s.{Request, Response}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import repositories.InMemoryRepository
import services.UserService
import org.http4s.syntax.kleisli._

object Banjo extends IOApp {

  def httpApp[F[_]: Sync]: Kleisli[IO, Request[IO], Response[IO]] =
    Router(
      "/v1" -> new PersonHttpEndpoint[IO](
        new UserService(new InMemoryRepository())
      ).personService,
    ).orNotFound

  import config.Config
  override def run(args: List[String]): IO[ExitCode] =
    Config.load().flatMap { cfg =>
      val serverCfg = cfg.server

      BlazeServerBuilder[IO]
        .bindHttp(serverCfg.port, serverCfg.host)
        .withHttpApp(httpApp[IO])
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    }
}
