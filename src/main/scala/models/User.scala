package models

case class UserId(id: Int)
case class Initials(name: String, surname: String)
case class User(id: UserId, initials: Initials)
