import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "noteables"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "com.typesafe.akka" % "akka-actor" % "2.10"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
