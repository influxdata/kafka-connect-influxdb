scalaVersion := "2.12.10"

name := "kafka-connect-influxdb"
organization := "com.influxdata"
version := "1.0"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.influxdb" % "influxdb-client-scala" % "1.5.0",
  "com.lihaoyi" %% "requests" % "0.5.1",
  "com.lihaoyi" %% "ujson" % "0.9.5",
  "com.typesafe.akka" %% "akka-actor" % "2.5.20",
  "com.typesafe.akka" %% "akka-stream" % "2.5.20",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.apache.kafka" % "connect-api" % "2.4.0",
  "org.scalactic" %% "scalactic" % "3.1.1"
)

val testcontainersScalaVersion = "0.35.2"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "org.apache.kafka" %% "kafka" % "2.4.0" % "test",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersScalaVersion % "test",
  "com.dimafeng" %% "testcontainers-scala-kafka" % testcontainersScalaVersion % "test",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.20" % "test"
)

publishArtifact in Test := true
publishArtifact in (Compile, packageDoc) := true
publishArtifact in (Compile, packageSrc) := true
publishArtifact in (Compile, packageBin) := true

fork in run := true

licenses += ("MIT", url(
  "https://github.com/influxdata/kafka-connect-influxdata/blob/master/LICENSE"
))
publishMavenStyle := true
