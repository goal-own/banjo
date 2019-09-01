lazy val baseSettings = Seq(
  name := "banjo-service",
  organization := "com.hackaton.goal.own",
  scalaVersion := "2.12.8",
  sbtVersion := "1.2.8",
  scalacOptions ++= Seq(
    "-feature",
    "-unchecked",
    "-deprecation",
    "-Xfatal-warnings",
    "-Ypartial-unification",
    "-language:higherKinds"
  )
)

lazy val http4sVersion = "0.20.0"
lazy val circeVersion = "0.11.1"
lazy val scalaTestVersion = "3.0.4"

lazy val root = (project in file("."))
  .settings(
    baseSettings,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-literal" % circeVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion
    )
  )
