package nullsafe

import org.scalatest.FunSuite

import scala.language.implicitConversions

class NullSafeTest extends FunSuite {

  test("list ::") {
    inverse_macros.transform {
      assertResult(List(10))(10 :: Nil)
      intercept[NullPointerException](10 :: null.asInstanceOf[List[Int]])
      assertResult(null)(10 :: null.asInstanceOf[List[Int]].?)
    }
  }

  test("string +") {
    inverse_macros.transform {
      assertResult("null10")(null.asInstanceOf[String] + 10)
      assertResult(null)(null.asInstanceOf[String].? + 10)
    }
  }
}
