import Implicits._

lazy val root = project.aggregated(".")(plugin, test).settings(name := "repair owner chain")

val plugin = project.compilerPlugin("plugin").dependsOnParadise.setDefaultCorePlugin()
val test = project.common("test", "test").makeSureOfDependency(plugin)
