package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._


import scala.concurrent.duration._

import scala.concurrent._

//import akka.actor.{Actor,ActorRef,Props}

// Necessary for `actor ? message`
import akka.pattern.ask 
//import akka.pattern.tell 

import akka.util.Timeout

// Use the Applications Default Actor System
import play.libs.Akka.system

import akka.actor._
import akka.routing.RoundRobinRouter





class Application() extends Controller {

    implicit val timeout = Timeout(5.seconds)

    def index = Action { implicit request =>

        println("Step 1")

        val system = ActorSystem("TrafficLights")
 
        println("Step 2")
        // create the master
        val master = system.actorOf(Props(new Master()),
          name = "master")

        println("Step 3")

        val respx = master ? Calculate(5)

        println("Step 4")


        Async{
            respx.map{
                num => Ok("Valor: " + num)
            }
        } 

        
    }
 
  sealed trait PiMessage
  case class Calculate(n: Double) extends PiMessage
  case class Work(n: Double) extends PiMessage
  case class Result(n: Double) extends PiMessage
  case class PiApproximation(pi: Double, duration: Duration)
  case class output(pi: Double)

  class simpleActor extends Actor{

    def receive = {
        case _ =>
        println("worked")
    }

  }
 
  class Worker extends Actor {
 
 
    def receive = {
      case Work(n) =>
        println("Step 7")
        sender ! 2*n // perform the work
    }
  }
 
  class Master extends Actor {
 
    //val workerRouter = context.actorOf(
    //    Props[Worker].withRouter(RoundRobinRouter(nrOfInstances = 5)), name = "workerRouter")

val simpleRouted = system.actorOf(Props[simpleActor].withRouter(
                        RoundRobinRouter(nrOfInstances = 10)
                     ), name = "simpleRoutedActor")
    


    //this.listener2
    def receive = {
        case Calculate(n) =>
            val caller = sender
            println("Step 5")
            val worker = context.actorOf(Props(new Worker()))
            println("Step 6")
            (worker ? Work(n)).mapTo[Double].map {
                num =>
                println(num)
                println("===")
                caller ! num
            }            

    }
 
  }
 

}

