import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val scalaMock = "org.mockito" % "mockito-all" % "1.10.19"
  lazy val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.5"
  lazy val json4sNative = "org.json4s" %% "json4s-native" % "3.5.1"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.6"
  lazy val kafka = "org.apache.kafka" % "kafka_2.12" % "0.10.2.1" excludeAll(
      ExclusionRule(organization = "org.slf4j"),
      ExclusionRule(organization = "com.sun.jdmk"),
      ExclusionRule(organization = "com.sun.jmx"),
      ExclusionRule(organization = "javax.jms")
    )
  lazy val guice = "com.google.inject" % "guice" % "4.1.0"
  lazy val scalaGuice = "net.codingwell" %% "scala-guice" % "4.1.0"
  lazy val logger = "com.typesafe.akka" % "akka-slf4j_2.12" % "2.4.18"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val health = "io.github.lhotari" %% "akka-http-health" % "1.0.8"
}
