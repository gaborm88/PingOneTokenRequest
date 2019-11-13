name := """pingoneprototype"""
organization := "fi.open"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

libraryDependencies += "commons-codec" % "commons-codec" % "1.10"
libraryDependencies += ws

libraryDependencies += "com.auth0" % "java-jwt" % "3.8.3"
libraryDependencies += "com.auth0" % "jwks-rsa" % "0.9.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "fi.open.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "fi.open.binders._"
