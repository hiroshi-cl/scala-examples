name := "scala-method-logger"

version := "1.0"

scalaOrganization := "org.scala-lang"

scalaVersion := "2.11.7"

crossVersion := CrossVersion.full

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-explaintypes", "-Xlint", "-Yinline-warnings", "-optimise")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies <++= (scalaOrganization, scalaVersion) { (org, ver) => Seq(
  org % "scala-compiler" % ver,
  org % "scala-library" % ver,
  org % "scala-reflect" % ver
)
}

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % Test

libraryDependencies += "junit" % "junit" % "4.12" % Test

publishTo := Some(Resolver.file("file", file("target") / "repo"))

organization := "jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi"

version := "1.0.0-SNAPSHOT"
