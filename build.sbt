val scala2_12Version = "2.12.8"
val http4sVersion = "0.20.6"

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

lazy val telegram4s = sonatypeProject("telegram4s", file("./telegram4s"))
  .settings {
    version := projectVersion
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion
    )
  }

lazy val examples = Project("telegram4s-examples", file("./examples"))
  .dependsOn(telegram4s)
  .settings {
    skip in publish := true
    publish := {}
    publishLocal := {}
    libraryDependencies ++= Seq()
  }

lazy val root = Project(id = "root", base = file("."))
  .aggregate(telegram4s, examples)
  .settings {
    name := "root"
    version := projectVersion
    scalaVersion := scala2_12Version
    scalacOptions += "-Ypartial-unification"
    cancelable := true
    isSnapshot := snapshot
    skip in publish := true
    publish := {}
    publishLocal := {}
  }
