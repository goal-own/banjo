package endpoints

import java.util.UUID

import cats.effect.Sync
import models.SessionModel.{Session, SessionId, Token}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.syntax.flatMap._
import services.SessionService

class SessionEndpoint[F[_]: Sync](sessionService: SessionService[F])
    extends Http4sDsl[F] {

  object TokenMatcher extends OptionalQueryParamDecoderMatcher[String]("token")
  object UserId extends OptionalQueryParamDecoderMatcher[Int]("userId")
  val sessionRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "session" ?: TokenMatcher(token) & UserId(id) =>
      token.fold(BadRequest("token was not sent")) { token =>
        sessionService.findByParam(Token(token)).flatMap {
          case Some(session) =>
            BadRequest(s"this token : ${session.token} is actually used!")
          case None => ???
        }
        Ok()
      }
  }
}
