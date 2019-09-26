import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}
import cats.syntax.all._
import models.{Age, AlreadyExistsException, PersonId, TestPerson, Username}

class ServicesSpec extends FlatSpec with Matchers {

  "person service" should "raise errors in some cases" in {
    import models.NotExistsException
    import repositories.InMemoryRepository
    import services.TestPersonService

    val service = TestPersonService[IO](new InMemoryRepository())
    val testCase = TestPerson(PersonId(0), Username("Pahnik98"), Age(21))

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
