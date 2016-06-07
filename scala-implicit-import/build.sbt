import Implicits._

lazy val root = project.aggregated(".")(core, test_engine).settings(name := "implicit import")

val core_plugin = project.compilerPlugin("plugin").dependsOnParadise.setDefaultCorePlugin()
val core_engine = project.imEngine("engine").usesParadise.setDefaultCoreEngine()
lazy val core = project.aggregated("core")(core_plugin, core_engine)

lazy val test_engine              = project.imEngineTest("engine")