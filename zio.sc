import zio.{Console, IO, Task, UIO, ZIO}

object legacy {
  def login(
             onSuccess: User => Unit,
             onFailure: AuthError => Unit
           ): Unit = ???
}

val login: IO[AuthError, User] =
  IO.async[AuthError, User] { callback =>
    legacy.login(
      user => callback(IO.succeed(user)),
      err  => callback(IO.fail(err))
    )
  }


