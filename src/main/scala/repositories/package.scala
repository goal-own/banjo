package object repositories {

  /**
    * Base trait for repositories
    */
  trait Repository[F[_], T] {
    def findAll: F[List[T]]
    def find(value: T): F[Option[T]]
    def persist(t: T): F[Unit]
    def delete(t: T): F[Unit]
  }
}
