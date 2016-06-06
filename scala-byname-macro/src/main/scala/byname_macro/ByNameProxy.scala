package byname_macro

case class ByNameProxy[Dispatch, Transformed](val a: Transformed) extends AnyVal
