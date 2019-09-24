package object repositories {

  /**
    * Base trait for repositories
    */
  trait Repository[F[_], E, P, I] {
    def update(e: E): F[Int]
    def findAll: F[List[E]]
    def findById(id: I): F[Option[E]]
    def findByParam(param: P): F[Option[E]]
    def persist(e: E): F[Int] // returns number of affected rows
    def delete(e: E): F[Int]
  }
}
