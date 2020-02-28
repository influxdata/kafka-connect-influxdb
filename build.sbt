scalaVersion := "2.12.10"

name := "kafka-connect-influxdb"
organization := "com.influxdata"
version := "1.0"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.influxdb" % "influxdb-client-scala" % "1.5.0"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "org.apache.kafka" % "connect-api" % "2.4.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test"

publishArtifact in Test := true
publishArtifact in (Compile, packageDoc) := true
publishArtifact in (Compile, packageSrc) := true
publishArtifact in (Compile, packageBin) := true

fork in run := true

licenses += ("MIT", url(
  "https://github.com/influxdata/kafka-connect-influxdata/blob/master/LICENSE"
))
publishMavenStyle := true
