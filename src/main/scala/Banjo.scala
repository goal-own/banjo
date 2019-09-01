import cats.effect.{ExitCode, IO, IOApp}
import endpoints.PersonHttpEndpoint
import org.http4s.server.blaze.BlazeServerBuilder
import repositories.InMemoryRepository
import services.UserService

class Banjo extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(
        new PersonHttpEndpoint[IO](new UserService(new InMemoryRepository()))
      )
      .serve
      .compile
      .drain
}
