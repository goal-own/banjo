package models
case class Username(username: String)
case class Age(age: Int)
case class Id(id: Long)
case class TestPerson(id: Id, username: Username, age: Age)
