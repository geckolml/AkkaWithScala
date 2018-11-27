package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MultithreadingRecap extends App {

  // Creating threads on the JVM
  val aThread = new Thread(() => println("I'm running in parallel"))
  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 1000).foreach(_ => println("hello")))
  val threadGoodBye = new Thread(() => (1 to 1000).foreach(_ => println("bye")))

  threadHello.start()
  threadGoodBye.start()

  // different runs produce different results!
  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.amount -= money

    def safeWithdraw(money: Int) = this.synchronized {
      this.amount -= money
    }
  }

  /*
    BA (10000)

    T1 -> withdraw = 1000
    T2 -> withdraw 0 2000

    T1 -> this.amount = this.amount - .... // PREEMPTED by the OS
    T2 -> this.amount = this.amount - 2000 = 8000
    T1 -> -1000 = 9000

    => result = 9000

    this.amount - 1000 is NOT ATOMIC

   */

  // inter-threda communication on the JVM
  // wait - notify mechanism

  // Scala Futures
  import scala.concurrent.ExecutionContext.Implicits.global

  val future = Future {
    // long computation - on a different thread
    42
  }

  // callbacks
  future.onComplete {
    case Success(42) => println("I found the meaning of life")
    case Failure(_) => println("something happened with the meaning of life!")
  }

  val aProcessedFuture = future.map(_ + 1) // Future with 43
  val aFlatFuture = future.flatMap { value =>
    Future(value + 2)
  } //Future with 44


  val filteredFuture = future.filter(_ % 2 == 0) // NoSuchElementException

  // for comprenhensions
  val aNonsenseFuture = for {
    meaningOfLife <- future
    filteredMeaning <- filteredFuture
  } yield meaningOfLife + filteredMeaning

  // andThen, recover/recoverWith

  // Promises
}