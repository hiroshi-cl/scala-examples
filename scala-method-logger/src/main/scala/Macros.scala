import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class loggerBundle(val c: whitebox.Context) {

  import c.universe._

  def impl(annottees: Tree*): Tree = {
    val DefDef(mods, name, tparams, vparamss, tpt, rhs) :: Nil = annottees
    val newName = TermName(name + "$" + "log")
    val created = DefDef(mods, name, tparams, vparamss, tpt,
      q"$newName[..${tparams.map(_.name)}](...${vparamss.map(_.map(_.name))})")
    val copied = DefDef(mods, newName, tparams, vparamss, tpt, rhs)
    q"$created; $copied; ()"
  }
}

class logger extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro loggerBundle.impl
}
