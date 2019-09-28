package repositories

import cats.effect.Effect
import models.{Stories, StoriesPath}
import doobie.util.transactor.Transactor
import doobie.implicits._
import models.SessionModel.SessionId

class ImageStoreRepository[F[_]: Effect](transactor: Transactor[F]) {
  def findStories(s: SessionId): F[Option[StoriesPath]] =
    sql"SELECT public.stories.stories FROM public.stories WHERE stories.session_id = ${s.sessionId.toString}"
      .query[StoriesPath]
      .option
      .transact(transactor)

  def persistStories(stories: Stories): F[Int] =
    sql"INSERT INTO public.stories (session_id, stories) VALUES (${stories.sessionId}, ${stories.path.value})".update.run
      .transact(transactor)
}
