
object Test extends App {
  @logger
  def hello(a: Int) = a

  println(hello(10))
}