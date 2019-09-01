import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}
import cats.syntax.all._

class ServicesSpec extends FlatSpec with Matchers {

  "person service" should "raise errors in some cases" in {
    import models.{
      User,
      Id,
      Username,
      Age,
      AlreadyExistsException,
      NotExistsException
    }
    import repositories.InMemoryRepository
    import services.UserService

    val service = UserService[IO](new InMemoryRepository())
    val testCase = User(Id(0), Username("Pahnik98"), Age(21))

    val persist = service.persist(testCase)
    val delete = service.delete(testCase)

    the[AlreadyExistsException] thrownBy {
      (persist >> persist).unsafeRunSync()
    } should have message (s"user: $testCase already exists")

    the[NotExistsException] thrownBy {
      (delete >> delete).unsafeRunSync()
    }
  }
}
