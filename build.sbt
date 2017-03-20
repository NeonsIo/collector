import Dependencies._
import sbt.Keys._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.neons",
      scalaVersion := "2.12.1",
      version      := "0.0.1"
    )),
    coverageEnabled := true,
    name := "Collector",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      json4sNative,
      akkaHttp,
      guice,
      scalaGuice,
      redis,
      akkaHttpTestKit
    ),
    mainClass in Compile := Some("io.neons.collector.Application")
  )

