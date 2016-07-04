
object Test extends App {

  def logger[X, R](f: X => R) = (x: X) => {
    println(x)
    val r = f(x)
    println(r)
    r
  }

  @decorator[Int, Int](logger)
  def hello(a: Int) = a

  @decorator[Int, Int => Int](logger)
  def bye(a: Int)(b: Int) = a

  @decorator[Int, Int](logger)
  def ret(a: Int): Int = {
    println("ret")
    if(true)
      return a

    a
  }

  // TODO
//  @logger
//  def poly[A](a: A) = a

  println(hello(10))
  println(bye(20)(30))
  println(ret(30))
//  println(poly(40))
}