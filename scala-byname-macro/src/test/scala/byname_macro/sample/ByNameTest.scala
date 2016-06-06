package byname_macro
package sample

import org.scalatest.FunSuite

class ByNameTest extends FunSuite {

  test("by-name") {
    import ByName._
    assert(tree(println(19)) == "scala.this.Predef.println(19) : ByName")
    assert(length(println(19)) == tree(println(19)).length)
    assert(tree(13 + 20 + "31".toInt) == "33.+(scala.this.Predef.augmentString(\"31\").toInt) : ByName")
  }

  test("pass to another by-name") {
    import ByName._

    def hoge(thunk: => Any)(implicit s: Unit => String) = tree(thunk)

    assert(tree(13 + 20 + "31".toInt) == hoge(13 + 20 + "31".toInt))
  }

  test("by-name parameter passing") {
    try {
//      parse("def piyo(thunk: => Any) {test(thunk)}")
      fail("error is expected")
    } catch {
      case e: Throwable =>
    }
  }

  test("by-name with proxy") {
    import ByNameWithProxy._

    assert(dispatching(println(19)) == "scala.this.Predef.println(19) : ByNameWithProxy.u : ByNameWithProxy")

    implicit val l: List[Int] = List(10)
    def piyo(thunk: => Any)(implicit s: ByNameProxy[List[Int], Unit => String]) = s.a()


    assert(piyo(println(19)) == "scala.this.Predef.println(19) : l : ByNameWithProxy")
    assert(piyo(println(19)) != dispatching(println(19)))
  }

  test("pass to another by-name with proxy") {
    import ByNameWithProxy._

    def piyopiyo(thunk: => Any)(implicit s: ByNameProxy[Unit, Unit => String]) = dispatching(thunk)

    assert(piyopiyo(println(19)) == dispatching(println(19)))
  }

  test("by-name (raw return type)") {
    import ByNameRaw._

    assert(raw(println(19)) == "scala.this.Predef.println(19) : ByNameRaw")
    assert(raw(13 + 20 + "31".toInt) == "33.+(scala.this.Predef.augmentString(\"31\").toInt) : ByNameRaw")
  }

  test("by-name with proxy (complex)") {
    import ByNameWithComplexProxy._

    assert(dispatching[Int, String](19 + "hoge".length) ==
      (23, "hoge:	ByNameWithComplexProxy : 19.+(\"hoge\".length()) : ByNameWithComplexProxy.u"))

//    assert(expectException(applied(ByNameWithComplexProxy)("dispatching")(19 + "hoge".length)))
  }
}
