lazy val baseSettings = Seq(
  name := "banjo service",
  version := "0.01",
  scalaVersion := "2.12.8",
  scalacOptions ++= Seq("-deprecation", "-Xfatal-warnings")
)
