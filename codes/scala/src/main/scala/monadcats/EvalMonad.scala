package monadcats

import cats.Eval

class EvalMonad {

}

object EvalMonad extends App {

  // val是eager且memorized, eager:不访问它,它也会执行
  val x = {
    println("Computing x")
    math.random()
  }
  println(x)
  println(x)

  // def是lazy且not memorized
  def y = {
    println("Computing y")
    math.random()
  }
  println(y)
  println(y)

  // lazy val是lazy且memorized
  lazy val z = {
    println("Computing z")
    math.random()
  }
  println(z)
  println(z)

  // similar to val - eager and memorized
  val now = Eval.now {
    println("Computing now")
    math.random()
  }
  println(now.value)
  println(now.value)
  // similar to lazy val - lazy and memorized
  val later = Eval.later {
    println("Computing later")
    math.random()
  }
  println(later.value)
  println(later.value)
  // similar to def - lazy and not memorized
  val always = Eval.always {
    println("Computing always")
    math.random()
  }
  println(always.value)
  println(always.value)

  val greeting = Eval
    .always {println("Step 1"); "Hello"}
    .map { str => println("Step 2"); s"$str world" }

  println(greeting.value)

  val ans = for {
    a <- Eval.now { println("Calculating A"); 40}
    b <- Eval.always {println("Calculating B"); 2}
  } yield {
    println("Adding A and B")
    a + b
  }

}
