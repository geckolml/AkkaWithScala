package part1recap

object GeneralRecap extends App{

  val aCondition: Boolean = false

  var aVariable = 42
  aVariable += 1 // aVariable 43

  // expressions
  val aConditionedVal = if (aCondition) 42 else 65

  //code block
  val aCodeBlock = {
    if (aCondition) 74
    56
  }

  // types
  // Unit
  val theUnit = println("Hello, Scala")

  def aFunction(x:Int): Int = x + 1
  // recursion - TAIL recursion
  def factorial(n: Int, acc: Int):Int =
    if (n <= 0) acc
    else factorial(n-1,acc*n)

  // OOP
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog

  trait Carnivore{
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore{
    override def eat(a: Animal): Unit = println("Crunch!")
  }

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog

  // anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("Roar")

  }

  aCarnivore eat aDog

  // generics
  abstract class MyList[+A]
  object MyList

  // case classes
  case class Person(name:String, age:Int) // a LOT in this course!

  //Exceptions
  val aPotentialFailure = try{
    throw new RuntimeException("I'm innocent, I swear!")// Nothing
  } catch {
    case e: Exception => "I caught an exception!"
  } finally {
    // side effects
    println("some logs")
  }

  // Functional programming

  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  val incremented = incrementer(42) // 43
  // incrementer.apply(42)

  val anonymousIncrementer = (x:Int) => x + 1
  // Int => Int === function1[Int, Int]

  // FP is all about working with functions as first-class
  List(1,2,3).map(incrementer)
  // map = HOF

  // for comprenhensions
  val pairs = for {
    num <- List(1,2,3,4)
    char <- List('a','b','c','d')
  } yield num + "-" + char

  // List(1,2,3,4).flatMap(num => List('a','b','c','d').map(char => num + "-" + char))

  // Seq, Array, List, Vector, Map, Tuples, Sets

  // "collections"
  // Option and Try
  val anOption = Some(2)
  val aTry = try{
    throw new RunTimeException
  }

  //pattern matching
}
