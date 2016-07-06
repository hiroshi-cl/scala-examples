import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class decoratorBundle(val c: whitebox.Context) {

  import c.universe._

  def impl(annottees: Tree*): Tree = {
    val DefDef(mods, name, tparams, vparamss, tpt, rhs) :: Nil = annottees
    val newName = c.freshName(name)
    val d = q"${c.prefix.tree.asInstanceOf[Apply].args.head}($newName _)(...${vparamss.map(_.map(_.name))})"
    val decorated = DefDef(mods, name, tparams, vparamss, tpt, d)
    val original = DefDef(mods, newName, tparams, vparamss, tpt, rhs)
    q"$decorated; $original; ()"
  }
}

class decorator[X, R](f: (X => R) => X => R) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro decoratorBundle.impl
}