package client

import cats.effect.{ConcurrentEffect, Sync}
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.{MediaType, Method, Request, Uri}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import cats.syntax.flatMap._
import cats.syntax.functor._
import org.http4s.client.Client
import org.log4s.Logger

import scala.concurrent.ExecutionContext

class ClientTest[F[_]: ConcurrentEffect: Sync](client: Client[F])(
  implicit logger: Logger
) extends Http4sDsl[F] {

  def makeRequest: Unit = {
    logger.info("HEre!!")
    val req = Request[F](
      method = Method.POST,
      uri = Uri.uri("https://vk-hackathon-gateway.trbna.com/ru/graphql/"),
    ).withContentType(`Content-Type`(MediaType.application.json))
      .withEntity(
        "{\n\tstat_tournament(id: \"epl\") {\n    name\n    currentSeason {\n      matches {\n        venue{name}\n        scheduledAt\n        currentMinute\n        status\n        home{score,team{id,name,country{name},logo{main}}}\n\t\t\t\taway{score,team{id,name,country{name},logo{main}}}\n      \treferees{name}\n      }\n    }\n  }\n}"
      )

    // use `client` here and return an `IO`.
    // the client will be acquired and shut down
    // automatically each time the `IO` is run.
    client.expect[String](req).flatMap { str =>
      Sync[F].delay(logger.info(s"It woks blya! $str"))
    }
  }
}
