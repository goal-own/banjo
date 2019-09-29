package mock

import java.util.UUID

import cats.effect.Sync
import models.SessionModel.SessionId
import repositories.ImageStoreRepository
import cats.syntax.flatMap._
import fs2._
import models.{Stories, StoriesId, StoriesPath}

import scala.util.Random

class InitFriends[F[_]: Sync](imageRepository: ImageStoreRepository[F]) {
  private val imagePaths =
    Range(0, 8).map(index => s"./images/$index").toList // mock

  def generateSessionId: F[SessionId] =
    Sync[F].delay { SessionId(UUID.randomUUID()) }

  def init: F[Unit] =
    imageRepository.findAllStories.flatMap { list =>
      if (list.size >= imagePaths.size) Sync[F].unit
      else
        Stream
          .emits(imagePaths)
          .covary[F]
          .evalMap { path =>
            generateSessionId.flatMap { x =>
              imageRepository.persistStories(
                Stories(StoriesId(Random.nextInt()), x, StoriesPath(path))
              )
            }
          }
          .compile // very bad
          .drain
    }
}

object InitFriends {
  val friends = Map(
    0 ->
      ("Павел" -> "Никонов"),
    1 -> ("Малик" -> "Хираев"),
    2 -> ("Данила" -> "Еременко"),
    3 -> ("Слава" -> "Васильева"),
    4 -> ("Крутой" -> "Человек"),
    5 -> ("Прекрасная" -> "Леди"),
    6 -> ("Красивая" -> "Женщина"),
    7 -> ("Спортивная" -> "Женщина")
  ) // mock
}
