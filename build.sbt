val scala2_12Version = "2.12.8"
val http4sVersion = "0.20.6"
val circeVersion = "0.11.1"
val catsVersion = "1.6.1"

lazy val snapshot: Boolean = true
val versionNumber = "0.0.1"

lazy val projectVersion: String = {
  if (!snapshot) versionNumber
  else versionNumber + "-SNAPSHOT"
}

cancelable in ThisBuild := true
organization in ThisBuild := "io.github.0lejk4"

def sonatypeProject(id: String, base: File) =
  Project(id, base)
    .settings(
      name := id,
      isSnapshot := snapshot,
      version := projectVersion,
      scalaVersion := scala2_12Version,
      publishTo := {
        val nexus = "https://oss.sonatype.org/"
        if (isSnapshot.value)
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases" at nexus + "service/local/staging/deploy/maven2")
      },
      updateOptions := updateOptions.value.withGigahorse(false),
      scalacOptions ++= Seq("-Ypartial-unification", "-feature"),
      resolvers += Resolver.sonatypeRepo("releases"),
      pomExtra :=
        <developers>
          <developer>
            <id>0lejk4</id>
            <name>Oleh Dubynskiy</name>
            <url>https://github.com/0lejk4/</url>
          </developer>
        </developers>
    )

lazy val core = sonatypeProject("telegram4s-core", file("./core"))
  .settings {
    version := projectVersion
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-generic-extras" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
    )
  }

lazy val examples = Project("telegram4s-examples", file("./examples"))
  .dependsOn(core)
  .settings {
    skip in publish := true
    publish := {}
    publishLocal := {}
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.0-RC10-1",
      "dev.zio" %% "zio-interop-cats" % "1.3.1.0-RC3",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
  }

lazy val root = Project(id = "telegram4s", base = file("."))
  .aggregate(core, examples)
  .settings {
    name := "telegram4s"
    version := projectVersion
    scalaVersion := scala2_12Version
    scalacOptions += "-Ypartial-unification"
    isSnapshot := snapshot
    skip in publish := true
    publish := {}
    publishLocal := {}
  }
