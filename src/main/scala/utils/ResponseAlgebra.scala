package utils

sealed trait ErrorCode { val code: Int }
object NotValidToken extends ErrorCode { val code = 1 }
object Fine extends ErrorCode { val code = 0 }

case class Resp[T](data: Option[T], errorCode: Int)
