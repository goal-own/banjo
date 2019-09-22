package utils

class ResponseAlgebra {
  sealed trait ErrorCode
  case class NotValidToken(code: Int = 1) extends ErrorCode
  case class Fine(code: Int = 0) extends ErrorCode

  case class Response[T](data: Option[T], errorCode: ErrorCode)
}
