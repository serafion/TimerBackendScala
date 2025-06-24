ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "TimerBackendScala"
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "org.scalatest" %% "scalatest" % "3.2.16" % Test,
      "org.scalactic" %% "scalactic" % "3.2.16" % Test
    )
  )
  .settings(
    // Enable Scala 3 features
    scalacOptions ++= Seq("-language:implicitConversions", "-language:postfixOps")
  )