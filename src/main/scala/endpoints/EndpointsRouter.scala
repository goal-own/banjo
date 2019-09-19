package endpoints

import cats.effect.Sync
import org.http4s.HttpRoutes
import repositories.InMemoryRepository
import services.UserService

/*
Creating instances for repositories and services
Combining http endpoints together
 */
object EndpointsRouter {

  def testEndpoint[F[_]: Sync]: HttpRoutes[F] =
    new PersonHttpEndpoint[F](new UserService(new InMemoryRepository())).personService
}
