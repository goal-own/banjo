package endpoints

import java.util.UUID

import cats.effect.Sync
import models.SessionModel.{Session, SessionId, Token, UserId}
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl
import cats.syntax.flatMap._
import services.SessionService
import io.circe.generic.auto._
import utils.{Fine, Resp}

import scala.util.Try

class SessionEndpoint[F[_]: Sync](sessionService: SessionService[F])
    extends Http4sDsl[F] {

  object TokenMatcher extends OptionalQueryParamDecoderMatcher[String]("token")
  object UserIdMatcher
      extends OptionalQueryParamDecoderMatcher[String]("user_id")

  val sessionRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root :? TokenMatcher(token) +& UserIdMatcher(userId) =>
      token.fold(BadRequest("token was not sent")) { token =>
        userId.fold(BadRequest("user_id was not sent"))(
          u =>
            Try(u.toInt).toOption.fold(BadRequest("user_id has wrong format"))(
              userId =>
                sessionService.findById(UserId(userId)).flatMap {
                  case Some(s) =>
                    sessionService
                      .update(s.copy(token = Token(token)))
                      .flatMap(result)
                  case None =>
                    Sync[F]
                      .delay { UUID.randomUUID() }
                      .flatMap { uuid =>
                        sessionService
                          .persist(
                            Session(
                              userId = UserId(userId),
                              token = Token(token),
                              sessionId = SessionId(uuid)
                            )
                          )
                          .flatMap(result)
                      }
              }
          )
        )
      }
  }

  private def result(res: Either[Throwable, Session]): F[Response[F]] = {
    res match {
      case Right(value) => Ok(Resp(Some(value.sessionId), Fine.code))
      case Left(_)      => InternalServerError()
    }
  }
}
