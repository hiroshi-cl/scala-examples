
object TestV2 extends App {

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

  println(hello(10))
  println(bye(20)(30))
  println(ret(30))
}