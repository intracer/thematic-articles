name := "thematic-articles"

version := "0.1"

scalaVersion := "2.12.5"

resolvers += "ImageJ" at "http://maven.imagej.net/content/repositories/thirdparty/"

libraryDependencies ++= Seq(
  "org.wikipedia" % "wiki-java" % "0.31",
  "net.openhft" % "chronicle-map" % "3.14.5",
  "com.github.tototoshi" %% "scala-csv" % "1.3.4",
  "org.specs2" %% "specs2-core" % "4.0.3" % Test
)

