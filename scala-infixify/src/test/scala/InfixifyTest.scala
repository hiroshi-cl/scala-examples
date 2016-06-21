import org.scalatest.FunSuite
import infixify._

class InfixifyTest extends FunSuite {
  test("postfixify") {
    val a = 10
    val b = 20
    assert(a + b == a :# (_ + b))
  }
  test("associativity") {
    val a = 10
    val b = 20
    val c = 30
    assert(a - b - c == a :# (_ - b) :# (_ - c))
  }
}

