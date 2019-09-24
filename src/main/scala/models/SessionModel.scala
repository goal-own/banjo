package models

import java.util.UUID

object SessionModel {
  case class Session(sessionId: SessionId, userId: UserId, token: Token)
  case class SessionId(id: UUID)
  case class UserId(id: Id)
  case class Token(accessToken: String)
}
