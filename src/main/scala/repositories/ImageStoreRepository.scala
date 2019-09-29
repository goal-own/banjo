package repositories

import cats.effect.Effect
import models.{Stories, StoriesId, StoriesPath}
import doobie.util.transactor.Transactor
import doobie.implicits._

class ImageStoreRepository[F[_]: Effect](transactor: Transactor[F]) {
  def findStories(s: StoriesId): F[Option[StoriesPath]] =
    sql"SELECT public.stories.stories FROM public.stories WHERE stories.stories_id = ${s.id}"
      .query[StoriesPath]
      .option
      .transact(transactor)

  def persistStories(stories: Stories): F[Int] =
    sql"INSERT INTO public.stories (stories_id, session_id, stories) VALUES (${stories.storiesId.id}, ${stories.sessionId}, ${stories.path.value})".update.run
      .transact(transactor)

  def findAllStories: F[List[Stories]] =
    sql"SELECT * FROM public.stories"
      .query[Stories]
      .to[List]
      .transact(transactor)
}
