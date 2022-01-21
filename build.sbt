ThisBuild / scalaVersion := "3.1.0"
ThisBuild / organization := "cloud.yantra.demo"

val circeVersion = "0.14.1"
val zioVersion = "2.0.0-RC1"

lazy val root =
  (project in file("."))
    .settings(
      name := "strimzi-kafka-apps",
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-core" % circeVersion,
        "io.circe" %% "circe-generic" % circeVersion,
        "io.circe" %% "circe-parser" % circeVersion,
        "dev.zio" %% "zio-nio" % zioVersion,
        "dev.zio" %% "zio-test" % zioVersion % Test,
//        "dev.zio" %% "zio-json" % "0.3.0-RC2",
        "io.circe" %% "circe-testing" % circeVersion % Test,
        "org.scalatest" %% "scalatest" % "3.2.10" % Test,
        "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test
      )
    )
