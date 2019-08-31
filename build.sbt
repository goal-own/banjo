lazy val baseSettings = Seq(
  name := "banjo service",
  scalaVersion := "2.12.8",
  organization := "io.github.goal.own",
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

lazy val http4sVersion = "0.20.10"
lazy val doobieVersion = "0.5.4"
lazy val circeVersion = "0.9.3"
lazy val scalaTestVersion = "3.0.5"

lazy val root = (project in file("."))
  .settings(
    baseSettings,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-literal" % circeVersion,
      "io.circe" %% "circe-optics" % circeVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion
    )
  )
