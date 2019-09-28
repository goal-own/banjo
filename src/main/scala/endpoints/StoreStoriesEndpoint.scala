package endpoints

import java.util.UUID

import cats.effect.{Blocker, ContextShift, ExitCode, Sync}
import org.http4s.dsl.Http4sDsl
import cats.syntax.functor._
import cats.syntax.flatMap._
import config.ServerConfig
import models.SessionModel.SessionId
import org.http4s._
import org.http4s.headers._
import org.log4s.Logger
import services.ImageService
import io.circe.generic.auto._

import scala.util.Try

class StoreStoriesEndpoint[F[_]: Sync: ContextShift](
  imageService: ImageService[F],
  blocker: Blocker,
  serverConfig: ServerConfig
)(implicit logger: Logger)
    extends Http4sDsl[F] {

  object SessionIdMatcher
      extends OptionalQueryParamDecoderMatcher[String]("session_id")
  object PathMatcher extends QueryParamDecoderMatcher[String]("path")

  val storeStoriesTestEndpoint: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "test" =>
      Ok(req.body.chunks)
        .map(_.putHeaders(`Content-Type`(MediaType.image.jpeg)))
  }

  val storeStoriesEndpoint: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "submit_image" :? SessionIdMatcher(sessionId) =>
      sessionId match {
        case None => BadRequest("session_id was not sent")
        case Some(id) =>
          Try(UUID.fromString(id)).toOption match {
            case None => BadRequest("session_id has wrong format")
            case Some(value) =>
              imageService
                .saveImage(SessionId(value), req.body)
                .flatMap {
                  case ExitCode.Success => Ok()
                  case _                => InternalServerError()
                }
          }
      }

    case GET -> Root / "get_stories" :? SessionIdMatcher(sessionId) =>
      sessionId match {
        case None => BadRequest("session_id was not sent")
        case Some(id) =>
          Try(UUID.fromString(id)).toOption match {
            case None => BadRequest("session_id has wrong format")
            case Some(value) =>
              imageService.findStories(SessionId(value)).flatMap {
                case Some(url) =>
                  Ok {
                    serverConfig.host + ":" + serverConfig.port + "/media" + "/get_image?path=" + url.value
                  }
                case None => BadRequest("don't have these session")
              }
          }
      }

    case GET -> Root / "get_image" :? PathMatcher(path) =>
      logger.info(s"$path")
      StaticFile
        .fromString(path, blocker.blockingContext)
        .getOrElseF(NotFound("File not found"))
  }
}
