package models

import java.util.UUID

import doobie.util.{Get, Put}

object SessionModel {
  case class Session(userId: UserId, token: Token, sessionId: SessionId)
  case class SessionId(id: UUID)
  case class UserId(id: Int) extends AnyVal
  case class Token(accessToken: String) extends AnyVal

  implicit val sessionIdGet: Get[SessionId] =
    Get[String].map(v => SessionId(UUID.fromString(v)))
  implicit val sessionIdPut: Put[SessionId] =
    Put[String].contramap(_.id.toString)
}
