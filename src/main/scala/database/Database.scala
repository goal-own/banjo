package database

import cats.effect.{Async, Blocker, ContextShift}
import config.DataBaseConfig
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

object Database {
  def transactor[F[_]: Async: ContextShift](dbConfig: DataBaseConfig,
                                            blocker: Blocker): Aux[F, Unit] =
    Transactor.fromDriverManager[F](
      dbConfig.driver,
      dbConfig.url,
      dbConfig.user,
      dbConfig.password,
      blocker
    )
}
