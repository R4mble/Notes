package functordemo

import cats.Functor
import cats.instances.function._
import cats.instances.list._
import cats.instances.option._
import cats.syntax.functor._

import scala.language.higherKinds

/**
  * @author Wangyl
  * @date 2019/8/20
  */
class FuntorDemo {

}


object FuntorDemo extends App {
  val func1: Int => Double =
    (x: Int) => x.toDouble

  val func2: Double => Double =
    (y: Double) => y * 2

//  println((func1 map func2)(1))
  println((func1 andThen func2)(1))
  println(func2(func1(1)))

  val list1 = List(1,2,3)
  val list2 = Functor[List].map(list1)(_ * 2)
  println(list2)
  println(list1.map(_ * 2))

  val option1 = Option(123)
  val option2 = Functor[Option].map(option1)(_ + 1)
  println(option2)

  val func = (x: Int) => x + 1
  // 把A => B的函数变为F[A] => F[B]
  val liftedFunc = Functor[Option].lift(func)
  println(liftedFunc(Option(1)))


  def doMath[F[_]](start: F[Int]) (implicit functor: Functor[F]): F[Int] =
    start.map(n => (n + 1) * 2)

  println(doMath(Option(20)))
  println(doMath(List(1,2,3)))
}
