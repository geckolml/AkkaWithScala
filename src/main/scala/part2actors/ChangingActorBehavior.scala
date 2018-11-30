package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ActorCapabilities.{Counter, system}
import part2actors.ChangingActorBehavior.Mom.MomStart

object ChangingActorBehavior extends App{

  object FussyKid {
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }

  class FussyKid extends Actor {
    import FussyKid._
    import Mom._

    // internal state of the kid
    var state = HAPPY
    override def receive:Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_) =>
        if (state == HAPPY) sender() ! KidAccept
        else sender() ! KidReject
    }
  }

  class StatelessFussyKid extends Actor {
    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false) // change my receive handler to sadReceive
      case Food(CHOCOLATE) =>
      case Ask(_) => sender() ! KidAccept
    }
    def sadReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false)// stay sad
      case Food(CHOCOLATE) => context.unbecome()
      case Ask(_) => sender() ! KidReject
    }
  }

  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String) // do you want to play?
    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {
    import Mom._
    import FussyKid._
    override def receive: Receive = {
      case MomStart(kidRef) =>
        // test out interaction
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("do you want to play?")
      case KidAccept => println("Yay, my kid is happy!")
      case KidReject => println("My kid is sad, but as he's healthy!")
    }
  }

  val system = ActorSystem("changingActorBehaviorDemo")
  val fussyKid = system.actorOf(Props[FussyKid])
  val statelessFussyKid = system.actorOf(Props[StatelessFussyKid])
  val mom = system.actorOf(Props[Mom])

  mom ! MomStart(statelessFussyKid)

  /**
    * mom receives Mom Start
    *   kid receives Food(veg) -> kid will change the handler to sadReceive
    *   kid receives Ask(play?) -> kid replies with the sadReceive handler =>
    * mom receives KidReject
    */

  /*

  context.become

      Food(veg) -> stack.push(sadReceive)
      Food(chocolate) ->  stack.push(happyReceive)

      Stack:
      1. happyReceive
      2. sadReceive
      3. happyReceive
   */

  /*
    new behavior
    Food(veg)
    Food(veg)
    Food(choco)
    Food(choco)

    Stack:
    1. sadReceive
    2. sadReceive
    2. happyReceive

   */

  /**
    * Exercises
    * 1 - Recreate the Counter Actor with context.become and NO MUTABLE STATE
    */

  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }


  class Counter extends Actor {
    import Counter._

    var count = 0

    override def receive: Receive = countReceive(0)

    def countReceive(currentCount: Int): Receive = {
      case Increment => context.become(countReceive(currentCount + 1))
      case Decrement => context.become(countReceive(currentCount - 1))
      case Print =>
    }
  }

  import Counter._
  val counter = system.actorOf(Props[Counter], "myCounter")

  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Decrement)
  counter ! Print

  /**
    * Exercises 2 - a simplified voting system
    */
  case class Vote(candidate: String)
  case object VoteStatusRequest
  case class VoteStatusReply(candidate: Option[String])
  class Citizen extends Actor {
    override def receive: Receive = ??? // TODO
  }

  case class AggregatorVotes(citizens: Set[ActorRef])
  class VoteAggregator extends Actor {
    override def receive: Receive = ??? // TODO
  }

  val alice = system.actorOf(Props[Citizen])
  val bob = system.actorOf(Props[Citizen])
  val charlie = system.actorOf(Props[Citizen])
  val daniel = system.actorOf(Props[Citizen])

  alice ! Vote("Martin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator])
  voteAggregator ! AggregatorVotes(Set(alice, bob, charlie, daniel))

  /*
     Print the status of the votes

     Martin -> 1
     Jonas -> 1
     Roland -> 2
   */

}
