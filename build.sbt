name := "akka-streams"

version := "1.0"

scalaVersion := "2.11.8"

val akkaV = "2.4.11"
val akkaStreamV = "2.4.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaStreamV
)
