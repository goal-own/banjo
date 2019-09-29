package client

import cats.effect.Sync
import org.http4s.{Headers, MediaType, Method, Request, Uri}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`

class Client[F[_]: Sync] extends Http4sDsl[F] {

//  def makeRequest: Unit = {
  //    val req = Request[F](
  //      method = Method.POST,
  //      uri = Uri.uri("https://vk-hackathon-gateway.trbna.com/ru/graphql/"),
  //    ).withContentType(`Content-Type`(MediaType.application.json))
  //      .withBody(
  //        "{\n\tstat_tournament(id: \"epl\") {\n    name\n    currentSeason {\n      matches {\n        venue{name}\n        scheduledAt\n        currentMinute\n        status\n        home{score,team{id,name,country{name},logo{main}}}\n\t\t\t\taway{score,team{id,name,country{name},logo{main}}}\n      \treferees{name}\n      }\n    }\n  }\n}"
  //      )
  //  }
}
