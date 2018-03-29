name := "thematic-articles"

version := "0.1"

scalaVersion := "2.12.5"

resolvers += "ImageJ" at "http://maven.imagej.net/content/repositories/thirdparty/"

libraryDependencies ++= Seq(
  "org.wikipedia" % "wiki-java" % "0.31",
  "net.openhft" % "chronicle-map" % "3.14.5"
)

