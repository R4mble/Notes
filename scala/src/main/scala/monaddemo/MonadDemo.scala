package monaddemo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Monad法则
  * Left identity: pure(a).flatMap(func) == func(a)
  * Right identity: m.flatMap(pure) == m
  * Associativity: m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
  */
trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  // 方便连续计算, 从盒子里拿出a来用好再塞回去
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => pure(f(a)))
}




object Monad extends App {

}


object MonadDemo extends App {

  def parseInt(str: String): Option[Int] =
    scala.util.Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] =
    if (b == 0) None else Some(a / b)

  def stringDivideBy(aStr: String, bStr: String): Option[Int] =
    parseInt(aStr).flatMap { aNum =>
      parseInt(bStr).flatMap{ bNum =>
        divide(aNum, bNum)
      }
    }

  println(stringDivideBy("6", "2"))
  println(stringDivideBy("6", "0"))
  println(stringDivideBy("dafs", "2"))

  def stringDivideByFor(aStr: String, bStr: String): Option[Int] =
    for {
      aNum <- parseInt(aStr)
      bNum <- parseInt(bStr)
      ans <- divide(aNum, bNum)
    } yield ans

  println(stringDivideByFor("6", "2"))
  println(stringDivideByFor("6", "0"))
  println(stringDivideByFor("dafs", "2"))

  val res = for {
    x <- (1 to 3).toList
    y <- (4 to 5).toList
  } yield (x, y)
  println(res)

  // flatMap就是序列化计算
  def doSomethingLongRunning: Future[Int] = Future(10)
  def doSomethingElseLongRunning: Future[Int] = Future(3)

  def doSomethingVeryLongRunningFor: Future[Int] =
    for {
      result1 <- doSomethingLongRunning
      result2 <- doSomethingElseLongRunning
    } yield result1 + result2

  def doSomethingVeryLongRunning: Future[Int] =
    doSomethingLongRunning.flatMap { result1 =>
      doSomethingElseLongRunning.map { result2 =>
        result1 + result2
      }
    }

}
