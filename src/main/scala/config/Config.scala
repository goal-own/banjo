package config

import cats.MonadError
import cats.effect.Sync
import cats.implicits._
import pureconfig._
import pureconfig.generic.auto._ // necessary for auto-derivation. do not remove
import pureconfig.error.ConfigReaderException

/*
Data types should be increased
 */
case class ServerConfig(port: Int, host: String)
case class DataBaseConfig(driver: String,
                          url: String,
                          user: String,
                          password: String)
case class BanjoConfig(server: ServerConfig, database: DataBaseConfig)

object Config {
  def load[F[_]](implicit E: MonadError[F, Throwable],
                 S: Sync[F]): F[BanjoConfig] =
    S.delay(ConfigSource.default.load[BanjoConfig]).flatMap { result =>
      result.fold(
        e => E.raiseError(new ConfigReaderException[BanjoConfig](e)),
        S.pure
      )
    }
}
