package endpoints
import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import services.PersonService

class PersonHttpEndpoint[F[_]: Sync](userService: PersonService[F])
    extends Http4sDsl[F] {
  val personService: HttpRoutes[F] = HttpRoutes {
    case GET -> Root           => ???
    case GET -> Root / name    => ???
    case req @ POST -> Root    => ???
    case DELETE -> Root / name => ???
  }
}
