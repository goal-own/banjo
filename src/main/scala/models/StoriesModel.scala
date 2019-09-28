package models

import models.SessionModel.SessionId

case class StoriesPath(value: String)
case class Stories(sessionId: SessionId, path: StoriesPath)
