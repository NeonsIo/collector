import Dependencies._
import sbt.Keys._

enablePlugins(DockerPlugin)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "neons",
      scalaVersion := "2.12.1",
      version      := "0.0.1"
    )),
    name := "collector",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      json4sNative,
      akkaHttp,
      guice,
      scalaGuice,
      kafka,
      akkaHttpTestKit,
      logback,
      logger,
      scalaMock
    ),
    mainClass in Compile := Some("io.neons.collector.application.akka.http.Application")
  )

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

imageNames in docker := Seq(
  ImageName(
    namespace = Some(organization.value),
    repository = name.value,
    tag = Some(version.value)
  )
)

buildOptions in docker := BuildOptions(
  cache = false,
  removeIntermediateContainers = BuildOptions.Remove.Always,
  pullBaseImage = BuildOptions.Pull.Always
)