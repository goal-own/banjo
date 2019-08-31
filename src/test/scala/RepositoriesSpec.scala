import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}
import cats.implicits._

class RepositoriesSpec extends FlatSpec with Matchers {

  "in-memory repository" should "right implement Repository methods" in {
    import repositories.InMemoryRepository
    import repositories.Person

    val repo = new InMemoryRepository[IO]()
    val testPerson = Person(0, "pashnik")

    (for {
      _ <- repo
        .find(testPerson)
        .flatMap(person => IO(person shouldBe None))
        .void
      _ <- repo.persist(testPerson)
      _ <- repo
        .find(testPerson)
        .flatMap(found => IO(found shouldBe Some(testPerson)))
        .void
      _ <- repo.delete(testPerson)
      _ <- repo.findAll.flatMap(found => IO(found shouldBe Nil))
    } yield ()).unsafeRunSync()
  }
}
