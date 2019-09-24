package object models {
  case class AlreadyExistsException(info: String) extends Exception(info)
  case class NotExistsException(info: String) extends Exception(info)
}
