package inverse_macros

import scala.reflect.macros.blackbox

class importWithCompanion extends IMImport {
  val fromClass = 10
}

object importWithCompanion {
  val fromModule = 10
}

class importWithoutCompanion extends IMImport {
  val fromClass = 10
}
