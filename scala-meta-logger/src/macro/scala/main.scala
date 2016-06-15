import scala.meta._

object main extends scala.annotation.Annotation {
  inline def apply()(defn: Any) = meta {
    val q"..$mods object $name extends { ..$early } with ..$base { $self => ..$stats }" = defn
    val main = q"def main(args: Array[String]): Unit = { ..$stats }"
    q"..$mods object $name extends { ..$early } with ..$base { $self => $main }"
  }
}
