package jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi.macros.byname.sample

/*
Copyright (c) 2014, Hiroshi Yamaguchi (Core Software Group)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi.macros.byname.ByNameProxy
import jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi.macros.debug._
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
    import ByName._
    try {
      parse("def piyo(thunk: => Any) {test(thunk)}")
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

    assert(expectException(applied(ByNameWithComplexProxy)("dispatching")(19 + "hoge".length)))
  }
}
