package byname_macro

trait UseContext {
  val c: scala.reflect.macros.whitebox.Context
}

trait AbstractBundle extends UseContext