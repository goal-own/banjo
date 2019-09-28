package models
case class Username(username: String)
case class Age(age: Int)
case class PersonId(id: Long)
case class TestPerson(id: PersonId, username: Username, age: Age)
