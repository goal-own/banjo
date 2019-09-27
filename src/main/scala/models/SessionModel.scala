package models

import java.util.UUID

import doobie.util.{Get, Put}
import org.http4s.QueryParamDecoder

object SessionModel {
  case class Session(userId: UserId, token: Token, sessionId: SessionId)
  case class SessionId(sessionId: UUID)
  case class UserId(id: Int) extends AnyVal
  case class Token(accessToken: String) extends AnyVal

  // chane to using meta TODO
  implicit val sessionIdGet: Get[SessionId] =
    Get[String].map(v => SessionId(UUID.fromString(v)))
  implicit val sessionIdPut: Put[SessionId] =
    Put[String].contramap(_.sessionId.toString)

  implicit val tokenMatcherParamDecoder: QueryParamDecoder[Token] =
    QueryParamDecoder[String].map(Token)

  implicit val userIdMatcherParamDecoder: QueryParamDecoder[UserId] =
    QueryParamDecoder[String].map(s => UserId(s.toInt))
}
