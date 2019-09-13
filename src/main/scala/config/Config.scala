package config

import cats.effect.IO
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
  def load(): IO[BanjoConfig] =
    IO(ConfigSource.default.load[BanjoConfig]).flatMap { result =>
      result.fold(
        e => IO.raiseError(new ConfigReaderException[BanjoConfig](e)),
        IO.pure
      )
    }
}
