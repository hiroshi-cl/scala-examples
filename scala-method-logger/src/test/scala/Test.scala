
object Test extends App {
  @logger
  def hello(a: Int) = a

  @logger
  def bye(a: Int)(b: Int) = a

  println(hello(10))
  println(bye(10)(20))
}