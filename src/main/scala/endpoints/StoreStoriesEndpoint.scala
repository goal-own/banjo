package endpoints

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl
import cats.syntax.functor._
import org.http4s._
import org.http4s.headers._
import org.log4s.Logger

class StoreStoriesEndpoint[F[_]: Sync](implicit logger: Logger)
    extends Http4sDsl[F] {

  val storeStoriesEndpoint: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "test" =>
      Ok(req.body.chunks)
        .map(_.putHeaders(`Content-Type`(MediaType.image.jpeg)))
  }
}
