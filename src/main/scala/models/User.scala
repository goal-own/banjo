package models

case class Username(username: String)
case class Age(age: Int)
case class Id(id: Long)
case class User(id: Id, username: Username, age: Age)

case class AlreadyExistsException(info: String) extends Exception(info)
case class NotExistsException(info: String) extends Exception(info)

