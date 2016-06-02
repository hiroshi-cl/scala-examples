import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class loggerBundle(val c: whitebox.Context) {

  import c.universe._

  def impl(annottees: Tree*): Tree = {
    val DefDef(mods, name, tparams, vparamss, tpt, rhs) :: Nil = annottees
    val newName = TermName(name + "$" + "log")
    val list = List(
      q"val r = $newName[..${tparams.map(_.name)}](...${vparamss.map(_.map(_.name))})",
      q"print(${name + " ("})",
      q"print(..${vparamss.flatten.map(_.name)})",
      q"print(${") = "})",
      q"println(r)",
      q"r"
    )
    val created = DefDef(mods, name, tparams, vparamss, tpt, q"..$list")
    val copied = DefDef(mods, newName, tparams, vparamss, tpt, rhs)
    q"$created; $copied; ()"
  }
}

class logger extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro loggerBundle.impl
}
