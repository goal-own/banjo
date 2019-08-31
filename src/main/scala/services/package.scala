package object services {

  /**
    * Base trait for services
    */
  trait Service[F[_], E] {
    def findAll: F[List[E]]
    def find(e: E): F[Option[E]]
    def persist(e: E): F[Unit]
    def delete(e: E): F[Unit] // maybe wrap Unit around Option first
  }
}
