import cats.effect.{ExitCode, IO, IOApp, Sync}
import endpoints.PersonHttpEndpoint
import cats.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import repositories.InMemoryRepository
import services.UserService
import org.http4s.syntax.kleisli._

object Banjo extends IOApp {

  def httpApp[F[_]: Sync] =
    Router(
      "/v1" -> new PersonHttpEndpoint[IO](
        new UserService(new InMemoryRepository())
      ).personService,
    ).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp[IO])
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
