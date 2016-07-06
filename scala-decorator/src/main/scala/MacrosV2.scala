import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class decoratorBundleV2(val c: whitebox.Context) {

  import c.universe._

  def impl(annottees: Tree*): Tree = {
    val DefDef(mods, name, tparams, vparamss, tpt, rhs) :: Nil = annottees
    val companion = TermName(
      constructor(c.prefix.tree.asInstanceOf[Apply].fun.asInstanceOf[Select].qualifier.asInstanceOf[New].tpt).toString)
    val newName = c.freshName(name)
    val d = q"$companion($newName _)(...${vparamss.map(_.map(_.name))})"
    val decorated = DefDef(mods, name, tparams, vparamss, tpt, d)
    val original = DefDef(mods, newName, tparams, vparamss, tpt, rhs)
    q"$decorated; $original; ()"
  }

  def constructor(tpt: Tree): Tree = tpt match {
    case AppliedTypeTree(t, _) => t
    case _ => tpt
  }
}

trait namedDecorator extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro decoratorBundleV2.impl
}

class logger extends namedDecorator

object logger {
  def apply[X, R](f: X => R) = (x: X) => {
    println(x)
    val r = f(x)
    println(r)
    r
  }
}
