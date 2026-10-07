ThisBuild / scalaVersion := "3.3.8"
ThisBuild / version      := "1.0.0"

// JavaFX ships native code per platform, so pick the matching classifier.
lazy val javaFxPlatform: String = {
  val os   = System.getProperty("os.name").toLowerCase
  val arm  = System.getProperty("os.arch") == "aarch64"
  if (os.startsWith("linux")) if (arm) "linux-aarch64" else "linux"
  else if (os.startsWith("mac")) if (arm) "mac-aarch64" else "mac"
  else if (os.startsWith("windows")) "win"
  else sys.error(s"Unsupported OS for JavaFX: $os")
}

lazy val root = (project in file("."))
  .settings(
    name := "scala-space-game",
    libraryDependencies ++= Seq(
      "org.scalafx"  %% "scalafx" % "21.0.0-R32",
      "org.scalameta" %% "munit"  % "1.1.1" % Test
    ),
    libraryDependencies ++= Seq("base", "controls", "graphics", "media").map { module =>
      "org.openjfx" % s"javafx-$module" % "21.0.5" classifier javaFxPlatform
    },
    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Wunused:all"),
    Compile / run / mainClass := Some("spacegame.SpaceGameApp"),
    // JavaFX must run in its own JVM, not inside sbt's.
    run / fork := true
  )
