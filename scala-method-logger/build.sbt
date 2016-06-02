name := "scala-method-logger"

version := "1.0"

scalaOrganization := "org.scala-lang"

scalaVersion := "2.11.7"

crossVersion := CrossVersion.full

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-explaintypes", "-Xlint", "-Yinline-warnings", "-optimise")

resolvers += "hiroshi-cl" at "https://hiroshi-cl.github.io/sbt-repos/"

addCompilerPlugin("jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi" % "core_plugin" % "3.0.0-SNAPSHOT" cross CrossVersion.full)

libraryDependencies <++= (scalaOrganization, scalaVersion) { (org, ver) => Seq(
  org % "scala-compiler" % ver,
  org % "scala-library" % ver,
  org % "scala-reflect" % ver
)
}

libraryDependencies += "jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi" % "core_engine" % "3.0.0-SNAPSHOT" cross CrossVersion.full

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % Test

libraryDependencies += "junit" % "junit" % "4.12" % Test

publishTo := Some(Resolver.file("file", file("target") / "repo"))

organization := "jp.ac.u_tokyo.i.ci.csg.hiroshi_yamaguchi"

version := "1.0.0-SNAPSHOT"