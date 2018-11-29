package part1recap

object ThreadModelLimitations extends App {


  /**
    *
    * Dr #1: OOP encapsulation is only valid in the SINGLE THREADED MODEL.
    */
  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.amount -= money
    def deposit(money: Int) = this.amount += money
    def getAmount = amount
  }

//  val account = new BankAccount(2000)
//  for(_ <- 1 to 1000){
//    new Thread(() => account.withdraw(1)).start()
//  }
//
//  for(_ <- 1 to 1000){
//    new Thread(() => account.deposit(1)).start()
//  }
//
//  println(account.getAmount)

  // OOP is broken in a multithreaded env
  // Synchronization! Locks to the rescue

  // deadlocks

  /**
    * DR #2: delegating something to a thread is a PAIN
    */

  // you have a running thread and you want to pass a runnable to that thread


  var task: Runnable = null

  val runningThread: Thread = new Thread(() => {
    while(true){
      while(task == null){
        runningThread.synchronized{
          println("[background] waiting for a task...")
          runningThread.wait()
        }
      }

      task.synchronized{
        println("[background] I have a task!")
        task.run()
        task = null

      }
    }
  })


  def delegateToBackgroundThread(r: Runnable): Unit ={
    if(task == null) task = r

    runningThread.synchronized{
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println(42))
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println("This should run in the background"))

  /**
    * DR #3: tracing and dealing with errors in a multithreaded env is PITN
    */
  // 1M numbers in between 10 threads
  
}
