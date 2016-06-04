name := "scala-macro-sbt"

// https://github.com/slick/slick/blob/master/project/Build.scala#L211
// http://stackoverflow.com/questions/25596360/what-does-extend-for-a-configuration-do

scalaOrganization := "org.scala-lang"

scalaVersion := "2.11.8"

crossVersion := CrossVersion.full

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-explaintypes", "-Xlint", "-Yinline-warnings", "-optimise")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val Macro = config("macro").hide.extend(Compile)

inConfig(Macro)(Defaults.compileSettings)

unmanagedClasspath in Compile <++= products in Macro

libraryDependencies <++= (scalaOrganization, scalaVersion) { (org, ver) => Seq(
  org % "scala-compiler" % ver,
  org % "scala-library" % ver,
  org % "scala-reflect" % ver
)
}

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % Test

libraryDependencies += "junit" % "junit" % "4.12" % Test

organization := "jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi"

version := "1.0.0-SNAPSHOT"
