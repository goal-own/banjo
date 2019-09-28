package endpoints

import cats.effect.{Blocker, ContextShift, Effect, Sync}
import config.ServerConfig
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.log4s.Logger
import repositories.{
  ImageStoreRepository,
  InMemoryRepository,
  SessionRepository
}
import services.{ImageService, SessionService, TestPersonService}

/*
Creating instances for repositories and services
Combining http endpoints together
 */
object EndpointsRouter {

  def testEndpoint[F[_]: Sync](implicit logger: Logger): HttpRoutes[F] =
    new TestPersonEndpoint[F](new TestPersonService(new InMemoryRepository())).personService

  def sessionEndpoint[F[_]: Effect](
    tr: Transactor[F]
  )(implicit logger: Logger): HttpRoutes[F] =
    new SessionEndpoint[F](new SessionService(new SessionRepository(tr))).sessionRoutes

  def storeStoriesEndpoint[F[_]: Effect: ContextShift](
    blocker: Blocker,
    serverConfig: ServerConfig,
    tr: Transactor[F]
  )(implicit logger: Logger): HttpRoutes[F] =
    new StoreStoriesEndpoint[F](
      new ImageService[F](new ImageStoreRepository[F](tr), blocker),
      blocker,
      serverConfig
    ).storeStoriesEndpoint
}
