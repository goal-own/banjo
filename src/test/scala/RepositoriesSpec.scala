import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}
import cats.implicits._
import models.{Age, TestPerson, Username}

class RepositoriesSpec extends FlatSpec with Matchers {

  "in-memory repository" should "right implement Repository methods" in {
    import models.Id
    import repositories.InMemoryRepository

    val repo = new InMemoryRepository[IO]()
    val testCase = TestPerson(Id(0), Username("Pahnik98"), Age(21))

    (for {
      _ <- repo
        .findById(testCase.id)
        .flatMap(u => IO(u shouldBe None))
        .void
      _ <- repo.persist(testCase)
      _ <- repo
        .findByParam(testCase.username)
        .flatMap(found => IO(found shouldBe Some(testCase)))
        .void
      _ <- repo.delete(testCase)
      _ <- repo.findAll.flatMap(found => IO(found shouldBe Nil))
    } yield ()).unsafeRunSync()
  }
}
