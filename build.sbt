val V = new {
  val scalaVersion = "2.13.1"
  val http4sVersion = "0.21.1"
  val circeVersion = "0.13.0"
  val scalaLogging = "3.9.2"
  val zio = "1.0.0-RC17"
  val zioCatz = "2.0.0.0-RC10"
  val logback = "1.2.3"
}

inThisBuild(List(
  organization := "io.github.0lejk4",
  homepage := Some(url("https://github.com/0lejk4/telegram4s")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "0lejk4",
      "Oleh Dubynskiy",
      "",
      url("https://github.com/0lejk4")
    )
  ),
  scalaVersion := V.scalaVersion,
))

name := "telegram4s"
skip in publish := true
cancelable in ThisBuild := true

lazy val core = Project("telegram4s-core", file("./core"))
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % V.http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % V.http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % V.http4sVersion,
      "org.http4s" %% "http4s-circe" % V.http4sVersion,
      "io.circe" %% "circe-generic" % V.circeVersion,
      "io.circe" %% "circe-generic-extras" % V.circeVersion,
      "io.circe" %% "circe-parser" % V.circeVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % V.scalaLogging,
      //      "dev.zio" %% "zio-streams" % V.zio,
      //      "dev.zio" %% "zio" % V.zio,
    ),
  )

lazy val examples = Project("telegram4s-examples", file("./examples"))
  .dependsOn(core)
  .settings(skip in publish := true)
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-streams" % V.zio,
      "dev.zio" %% "zio" % V.zio,
      "dev.zio" %% "zio-interop-cats" % V.zioCatz,
      "ch.qos.logback" % "logback-classic" % V.logback,
    ),
  )
