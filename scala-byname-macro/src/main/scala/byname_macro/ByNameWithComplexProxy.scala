package byname_macro

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

object ByNameWithComplexProxy {

  implicit def byNameProxySample[A, B, C]: ByNameProxy[A, String => (B, C)] = macro ByNameBundle.byNameProxy

  def apply[A, B](u: A, s: B): String => (B, String) = macro applyImpl

  def applyImpl(c: Context)(u: c.Tree, s: c.Tree) = {
    import c.universe._
    q"(in: String) => ($s, in + ${show(c.prefix.tree)} + ${" : "} + ${show(s)} + ${" : "} + ${show(u)})"
  }

  implicit val u: Unit = ()

  def dispatching[A, C](thunk: => A)(implicit s: ByNameProxy[Unit, String => (A, C)]) = s.a("hoge:\t")

}

