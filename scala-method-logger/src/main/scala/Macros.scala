import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class loggerBundle(val c: whitebox.Context) {
  import c.universe._

  def impl(annottees: Tree*): Tree = {
    val DefDef(mods, name, tparams, vparamss, tpt, rhs) :: Nil = annottees
    println(name)
    DefDef(mods, name, tparams, vparamss, tpt, rhs)
  }
}

class logger extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro loggerBundle.impl
}
