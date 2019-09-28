package endpoints
import cats.effect.Sync
import org.http4s.HttpRoutes
import services.{AlreadyExistsException, TestPersonService}
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import models.{TestPerson, Username}
import org.log4s.Logger

class TestPersonEndpoint[F[_]: Sync](userService: TestPersonService[F])(
  implicit logger: Logger
) extends Http4sDsl[F] {

  object TokenMatcher extends OptionalQueryParamDecoderMatcher[String]("a")
  object UserIdMatcher extends OptionalQueryParamDecoderMatcher[String]("b")

  val personService: HttpRoutes[F] = HttpRoutes
    .of[F] {
      case GET -> Root => Ok(userService.findAll)
      case GET -> Root / "a" :? TokenMatcher(a) +& UserIdMatcher(userId) =>
        logger.info("Pisdez" + a + userId)
        Ok()
      case GET -> Root / username =>
        userService
          .findByParam(Username(username))
          .flatMap(_.fold(NotFound("User not found"))(Ok(_)))
      case req @ POST -> Root =>
        req.decode[TestPerson] { user =>
          userService
            .persist(user)
            .flatMap(
              _ =>
                Created(
                  s"User created with username: ${user.username.username}"
              )
            )
            .handleErrorWith {
              case AlreadyExistsException(_) =>
                Conflict(s"Existent user with username: ${user.username}")
            }
        }
      case DELETE -> Root / username =>
        userService
          .findByParam(Username(username))
          .flatMap(
            found =>
              found.fold(NotFound(s"User with username: $username"))(
                userService.delete(_) >> NoContent()
            )
          )
    }
}
