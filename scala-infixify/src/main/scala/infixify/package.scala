package object infixify {

  implicit class InfixifiedPrefix[A](val a: A) extends AnyVal {
    @inline
    def :#[B](fun: A => B): B = fun(a)

    @inline
    def :![B](fun: A => B): B = fun(a)

    @inline
    def #:[B](fun: A => B): B = fun(a)
    
    @inline
    def !:[B](fun: A => B): B = fun(a)
  }

}
