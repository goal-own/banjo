package utils

import cats.effect.Sync
import fs2._

object StreamUtils {
  def evalF[F[_], A](f: => A)(implicit F: Sync[F]): Stream[F, A] =
    Stream.eval(F.delay(f))
  def eval[F[_], A](f: => F[A])(implicit F: Sync[F]): Stream[F, A] =
    Stream.eval(f)
}
