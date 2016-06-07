
import scala.reflect.macros._
import scala.language.experimental.macros

package object inverse_macros {

  private[this] class Bundle(val c: whitebox.Context) {
    import c.universe._

    def transformImpl(a: Tree): Tree = a
  }

  def transform[T](a: T): T = macro Bundle.transformImpl

}