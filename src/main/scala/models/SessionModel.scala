package models

import java.util.UUID

object SessionModel {
  case class Session(id: Id)
  case class Id(id: UUID)
  case class Token(accessToken: String)
}
