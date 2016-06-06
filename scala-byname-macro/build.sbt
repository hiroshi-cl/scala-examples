name := "scala-examples-byname-macro"

version := "1.0"

scalaOrganization := "org.scala-lang"

scalaVersion := "2.11.7"

crossVersion := CrossVersion.full

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-explaintypes", "-Xlint", "-Yinline-warnings", "-optimise")

lazy val Macro = config("macro").extend(Compile)

inConfig(Macro)(Defaults.compileSettings)

unmanagedClasspath in Compile <++= products in Macro

unmanagedClasspath in Test <++= products in Macro

libraryDependencies <++= (scalaOrganization, scalaVersion) { (org, ver) => Seq(
  org % "scala-compiler" % ver,
  org % "scala-library" % ver,
  org % "scala-reflect" % ver
)
}

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % Test

libraryDependencies += "junit" % "junit" % "4.12" % Test