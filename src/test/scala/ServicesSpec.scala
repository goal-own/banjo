import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}
import cats.syntax.all._

class ServicesSpec extends FlatSpec with Matchers {

  "person service" should "raise errors in some cases" in {
    import models.{AlreadyExistsException, NotExistsException, Person}
    import repositories.InMemoryRepository
    import services.PersonService

    val service = PersonService[IO](new InMemoryRepository())
    val testPerson = Person(1, "pashnik")

    val persist = service.persist(testPerson)
    val delete = service.delete(testPerson)

    the[AlreadyExistsException] thrownBy {
      (persist >> persist).unsafeRunSync()
    } should have message (s"person: $testPerson already exists")

    the[NotExistsException] thrownBy {
      (delete >> delete).unsafeRunSync()
    }
  }
}
