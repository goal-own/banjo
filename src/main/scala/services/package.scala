package object services {

  /**
    * Base trait for services
    */
  trait Service[F[_], E, P, I] {
    def update(e: E): F[Unit]
    def findById(id: I): F[Option[E]]
    def findByParam(param: P): F[Option[E]]
    def findAll: F[List[E]]
    def persist(e: E): F[Unit]
    def delete(e: E): F[Unit] // maybe wrap Unit around Option first
  }
}
