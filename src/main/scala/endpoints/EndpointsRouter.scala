package endpoints

import cats.effect.Sync
import org.http4s.HttpRoutes
import repositories.InMemoryRepository
import services.TestPersonService

/*
Creating instances for repositories and services
Combining http endpoints together
 */
object EndpointsRouter {

  def testEndpoint[F[_]: Sync]: HttpRoutes[F] =
    new TestPersonEndpoint[F](new TestPersonService(new InMemoryRepository())).personService

  def sessionEndpoint[F[_]: Sync]: HttpRoutes[F] =
    ???
}
