import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

package object endpoints {
  implicit def jsonDecoder[F[_]: Sync, A: Decoder]: EntityDecoder[F, A] =
    jsonOf[F, A]
  implicit def jsonEncoder[F[_]: Sync, A: Encoder]: EntityEncoder[F, A] =
    jsonEncoderOf[F, A]
}
