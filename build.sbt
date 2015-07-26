name := "akka-streams"

version := "1.0"

scalaVersion := "2.11.7"

val akkaV = "2.3.11"
val akkaStreamV = "1.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,
  "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV
)
