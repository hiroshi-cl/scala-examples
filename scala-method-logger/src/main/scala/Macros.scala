import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class loggerBundle(val c: whitebox.Context) {

  import c.universe._

  def impl(annottees: Tree*): Tree = {
    val DefDef(mods, name, tparams, vparamss, tpt, rhs) :: Nil = annottees
    val list = List(
      q"print(${name + " ("})",
      q"print(..${vparamss.flatten.map(_.name)})",
      q"val r = $rhs",
      q"print(${") = "})",
      q"println(r)",
      q"r"
    )
    DefDef(mods, name, tparams, vparamss, tpt, q"..$list")
  }
}

class logger extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro loggerBundle.impl
}
