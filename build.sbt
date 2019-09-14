lazy val baseSettings = Seq(
  name := "banjo-service",
  organization := "com.hackaton.goal.own",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.8",
  sbtVersion := "1.2.8",
  scalacOptions ++= Seq(
    "-feature",
    "-unchecked",
    "-deprecation",
    "-Xfatal-warnings",
    "-Ypartial-unification",
    "-language:higherKinds"
  ),
  resourceDirectory in Compile := baseDirectory.value / "resources"
)

lazy val http4sVersion = "0.20.0"
lazy val circeVersion = "0.11.1"
lazy val pureConfigVersion = "0.12.0"
lazy val scalaTestVersion = "3.0.4"
lazy val logbackVersion = "1.2.3"

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
      "org.scalatest" %% "scalatest" % scalaTestVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
    ).map(_ withSources () withJavadoc ())
  )
