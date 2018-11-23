package playground

import akka.actor.ActorSystem

object Playground extends App {

  val actorSystem = ActorSystem("Hello_Akka")
  println(actorSystem.name)

}
