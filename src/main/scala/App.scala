import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import repositories.TestRepository
import services.BanjoService

object App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(BanjoService[IO](TestRepository()).service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
