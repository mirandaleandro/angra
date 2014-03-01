import play.Project._
name := "Angra"

version := "1.0-SNAPSHOT"

resolvers ++= Seq(
  "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository",
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases",
  "fwbrasil.net" at "http://fwbrasil.net/maven/"
)

val activateVersion = "1.4.4"

libraryDependencies ++= Seq(
    jdbc,
    anorm,
    cache,
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "net.fwbrasil" %% "activate-play" % activateVersion,
    "net.fwbrasil" %% "activate-jdbc" % activateVersion,
    "net.fwbrasil" %% "activate-core" % activateVersion
)     

play.Project.playScalaSettings
