import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.4"
  lazy val json4sNative = "org.json4s" %% "json4s-native" % "3.5.0"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.3"
  lazy val redis = "com.github.etaty" %% "rediscala" % "1.8.0"
  lazy val guice = "com.google.inject" % "guice" % "4.1.0"
  lazy val scalaGuice = "net.codingwell" %% "scala-guice" % "4.1.0"
}
