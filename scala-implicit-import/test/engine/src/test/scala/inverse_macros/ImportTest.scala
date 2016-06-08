package inverse_macros

import debug.typeable
import org.scalatest.FunSuite

class ImportTest extends FunSuite {

  def withCompanion(i: Int@importWithCompanion) = 10

  def withoutCompanion(i: Int@importWithoutCompanion) = 10

  test("@IMImport") {
    transform {
      withCompanion(10)
      withoutCompanion(10)

      withCompanion(10 + fromClass + fromModule)
      withoutCompanion(10 + fromClass)
    }
  }

  test("@IMImport def") {
    @typeable("@inverse_macros.IMEngineApplied def f: Int = { ImportTest.this.withCompanion({ val at_imimport5: inverse_macros.importWithCompanion = new inverse_macros.importWithCompanion(); import at_imimport5._; import importWithCompanion._; 10 }); ImportTest.this.withoutCompanion({ val at_imimport6: inverse_macros.importWithoutCompanion = new inverse_macros.importWithoutCompanion(); import at_imimport6._; 10 }); ImportTest.this.withCompanion({ val at_imimport7: inverse_macros.importWithCompanion = new inverse_macros.importWithCompanion(); import at_imimport7._; import importWithCompanion._; 10.+(at_imimport7.fromClass).+(importWithCompanion.fromModule) }); ImportTest.this.withoutCompanion({ val at_imimport8: inverse_macros.importWithoutCompanion = new inverse_macros.importWithoutCompanion(); import at_imimport8._; 10.+(at_imimport8.fromClass) }) }")
    def f = {
      withCompanion(10)
      withoutCompanion(10)

      withCompanion(10 + fromClass + fromModule)
      withoutCompanion(10 + fromClass)
    }
  }

}
