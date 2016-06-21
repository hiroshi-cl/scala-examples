import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import infixify._

object InfixifyCheck extends Properties("Infixify") {
  property("postfixify") = forAll { (a: Int, b: Int) =>
    a + b == a :#  (_ + b)
  }
  property("associativity") = forAll { (a: Int, b: Int, c: Int) =>
    a - b - c == a :#  (_ - b) :# (_ - c)
  }
}

