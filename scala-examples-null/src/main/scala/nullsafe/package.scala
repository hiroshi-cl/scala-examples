package object nullsafe {

  implicit class NullSafe[T](val v: T) extends AnyVal {
    def ? : T@nullsafe = v
  }

}
