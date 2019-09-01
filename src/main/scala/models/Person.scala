package models

case class Person(id: Int, name: String)
case class AlreadyExistsException(info: String) extends Exception(info)
case class NotExistsException(info: String) extends Exception(info)
