name := "scala_cqrs_event_sourcing_test"

version := "1.0"
scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
lazy val akkaVersion = "2.5.3"
lazy val playVersion = "2.6.1"

resolvers += "Bintary JCenter" at "http://jcenter.bintray.com"

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-json" % playVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "play-circe" %% "play-circe" % "2.6-0.8.0"
)