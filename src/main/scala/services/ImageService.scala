package services

import java.nio.file.{Files, Path, Paths}
import java.util.UUID

import cats.effect.{Blocker, ContextShift, ExitCode, Sync}
import models.SessionModel.SessionId
import cats.syntax.flatMap._
import cats.syntax.functor._
import models.{Stories, StoriesPath}
import org.log4s.Logger
import fs2._
import repositories.ImageStoreRepository

class ImageService[F[_]: Sync](repo: ImageStoreRepository[F], blocker: Blocker)(
  implicit logger: Logger,
  rt: RaiseThrowable[F],
  ctx: ContextShift[F]
) {
  private val imagesDirectory = Paths.get("./images")

  private def file: F[Path] =
    Sync[F].delay(UUID.randomUUID()).map { uuid =>
      Paths.get(imagesDirectory.toString + "/" + uuid.toString + ".jpg")
    }

  def persist(s: Stories): F[Either[Throwable, Stories]] = {
    repo.findStories(s.sessionId).flatMap {
      case Some(_) =>
        Sync[F]
          .delay(Left(AlreadyExistsException(s"session: $s already exists")))
      case None =>
        affect(s, repo.persistStories(s))
    }
  }

  def findAll(e: SessionId): F[List[Stories]] = repo.findAllStories

  def findStories(s: SessionId): F[Option[StoriesPath]] =
    repo.findStories(s)

  def saveImage(sessionId: SessionId, stream: Stream[F, Byte]): F[ExitCode] = {
    Stream
      .eval(file)
      .flatMap { file =>
        val savePathDb =
          Stream
            .eval(persist(Stories(sessionId, StoriesPath(file.toString))))
            .rethrow // very bad should think about it!!
        val saveImage =
          stream.through(io.file.writeAll(file, blocker))
        val createDirectory =
          Stream.eval(
            Sync[F]
              .delay {
                if (!Files.exists(imagesDirectory))
                  Files.createDirectories(imagesDirectory)
              }
              .as(())
          )
        createDirectory ++ savePathDb ++ saveImage
      }
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
