package byname_macro

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

object ByNameRaw {

  implicit def byNameRawSample: String = macro ByNameBundle.byName

  def apply(s: Any): String = macro applyImpl

  def applyImpl(c: Context)(s: c.Tree) = {
    import c.universe._
    q"${show(s)} + ${" : "} + ${show(c.prefix.tree)}"
  }

  def raw(thunk: => Any)(implicit s: String) = s
}

