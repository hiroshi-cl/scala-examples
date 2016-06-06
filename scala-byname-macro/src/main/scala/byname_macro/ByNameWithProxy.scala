package byname_macro

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

object ByNameWithProxy {

  def apply[A](u: A, s: Any): String = macro applyImpl

  def applyImpl(c: Context)(u: c.Tree, s: c.Tree) = {
    import c.universe._
    q"${show(s)} + ${" : "} + ${show(u)} + ${" : "} + ${show(c.prefix.tree)}"
  }

  implicit val u: Unit = ()

  implicit def proxy[A]: ByNameProxy[A, () => String] = macro ByNameBundle.byNameProxy

  def dispatching(thunk: => Any)(implicit s: ByNameProxy[Unit, () => String]) = s.a()

}

