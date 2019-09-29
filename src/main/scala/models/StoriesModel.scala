package models

import models.SessionModel.SessionId

case class StoriesPath(value: String)
case class StoriesId(id: Int)
case class Stories(storiesId: StoriesId,
                   sessionId: SessionId,
                   path: StoriesPath)
