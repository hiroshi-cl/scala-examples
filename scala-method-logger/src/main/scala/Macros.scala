import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class helloBundle(val c: whitebox.Context) {
  import c.universe._

  def impl(annottees: Tree*): Tree = {
    val result = {
      annottees.toList match {
        case q"object $name extends ..$parents { ..$body }" :: Nil =>
          q"""
            object $name extends ..$parents {
              def hello: ${typeOf[String]} = "hello"
              ..$body
            }
          """
      }
    }
    result
  }
}

class hello extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro helloBundle.impl
}
