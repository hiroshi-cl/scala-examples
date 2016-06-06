package byname_macro.sample

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

private[sample] object ByName {

  implicit def byNameSample: Unit => String = macro ByNameBundle.byName

  def apply(s: Any): String = macro applyImpl

  def applyImpl(c: Context)(s: c.Tree) = {
    import c.universe._
    q"${show(s)} + ${" : "} + ${show(c.prefix.tree)}"
  }

  def tree(thunk: => Any)(implicit s: Unit => String) = s()

  implicit def enc(implicit s: Unit => String): Int = s().length

  def length(thunk: => Any)(implicit s: Int) = s

}
