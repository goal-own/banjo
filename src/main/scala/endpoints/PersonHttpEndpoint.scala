package endpoints
import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import models.{AlreadyExistsException, User, Username}
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import services.UserService
import org.http4s.circe._
import cats.syntax.all._
import org.http4s.dsl.Http4sDsl

class PersonHttpEndpoint[F[_]: Sync](userService: UserService[F])
    extends Http4sDsl[F] {

  private implicit def personDecoder: EntityDecoder[F, User] =
    jsonOf[F, User]
  private implicit def personEncoder: EntityEncoder[F, User] =
    jsonEncoderOf[F, User]

  private implicit def encoder: Encoder[User] = implicitly
  private implicit def decoder: Decoder[User] = implicitly

  val personService: HttpRoutes[F] = HttpRoutes
    .of[F] {
      case GET -> Root => Ok("should learn Circe")
      case GET -> Root / username =>
        userService
          .findByParam(Username(username))
          .flatMap(_.fold(NotFound("User not found"))(Ok(_)))
      case req @ POST -> Root =>
        req.decode[User] { user =>
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
