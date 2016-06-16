import scala.meta._

/*
//gave up!
object loggerMeta {
  inline def apply()(defn: Any) = meta {
    val q"$mods def $name[..$tparams](...$vparamss): $tpt = $rhs" = defn
    val newName = Term.Name(name + "$" + "log")

    val list = List(
      //      q"val r = $newName[..${tparams.map(x => Type.Name(x.name.value))}](...${vparamss.map(_.map(x => Term.Name(x.name.value)))})",
      (vparamss.foldLeft(
        Term.ApplyType(newName, tparams.map(x => Type.Name(x.name.value)))
      ) {
        p: (Term, Seq[Term.Param]) => Term.Apply(p._1, p._2.map(x => Term.Name(x.name.value))).asInstanceOf[scala.collection.immutable.Seq]
      }),
      q"print(${name + " ("})",
      q"print(..${vparamss.flatten.map(x => Term.Name(x.name.value))})",
      q"print(${") = "})",
      q"println(r)",
      q"r"
    )
    val created = q"$mods def $name[..$tparams](...$vparamss): $tpt = {..$list}"
    val copied = q"$mods def $newName[..$tparams](...$vparamss): $tpt = $rhs"
    Term.Block(Seq(created, copied))
    //    q"{ $created; $copied; () }"
  }
}
*/

/*
Macro paradise がバグってる…
@loggerMeta
def def hello(a: Int) = a
のようにしているのに
    val q"$mods def $name[..$tparams](...$vparamss): $tpt = $rhs" = defn
で defn に object がまるごと入ってる

scala.MatchError: object Test extends App {
  def hello(a: Int) = a
  def bye(a: Int)(b: Int) = a
  def ret(a: Int): Int = {
    println("ret")
    if (true) return a
    a
  }
  def poly[A](a: A) = a
  println(hello(10))
  println(bye(20)(30))
  println(ret(30))
  println(poly(40))
} (of class scala.meta.Defn$Object$DefnObjectImpl)

 */