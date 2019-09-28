package endpoints

import cats.effect.{Effect, Sync}
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.log4s.Logger
import repositories.{InMemoryRepository, SessionRepository}
import services.{SessionService, TestPersonService}

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

  def storeStoriesEndpoint[F[_]: Sync](implicit logger: Logger): HttpRoutes[F] =
    new StoreStoriesEndpoint[F]().storeStoriesEndpoint
}
