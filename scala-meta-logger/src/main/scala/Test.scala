
object Test extends App {
  @logger
  def hello(a: Int) = a

  @logger
  def bye(a: Int)(b: Int) = a

  @logger
  def ret(a: Int): Int = {
    println("ret")
    if(true)
      return a

    a
  }

  @logger
  def poly[A](a: A) = a

  println(hello(10))
  println(bye(20)(30))
  println(ret(30))
  println(poly(40))
}