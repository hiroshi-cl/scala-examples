package byname_macro.sample

import language.experimental.macros
import scala.reflect.macros.whitebox.Context
import language.higherKinds

private[sample] object ByNameRaw {

  implicit def byNameRawSample: String = macro ByNameBundle.byName

  def apply(s: Any): String = macro applyImpl

  def applyImpl(c: Context)(s: c.Tree) = {
    import c.universe._
    q"${show(s)} + ${" : "} + ${show(c.prefix.tree)}"
  }

  def raw(thunk: => Any)(implicit s: String) = s
}

