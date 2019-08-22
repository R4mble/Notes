package catsmonoid

import cats.{Monoid, Semigroup}
import cats.instances.string._
import cats.instances.int._
import cats.instances.option._
import cats.syntax.semigroup._

/**
  * @author Wangyl
  * @date 2019/8/20
  */
class CatsMonoid {

//  def add(items: List[Int]): Int =
//    items.foldLeft(Monoid[Int].empty)(_ |+| _)
//
//  def add[A](items: List[A])(implicit monoid: Monoid[A]): A =
//    items.foldLeft(monoid.empty)(_ |+| _)

  def add[A: Monoid](items: List[A]): A =
    items.foldLeft(Monoid[A].empty)(_ |+| _)

  implicit val monoid: Monoid[Order] = new Monoid[Order] {
    def combine(o1: Order, o2: Order) =
      Order (
        o1.totalCost + o2.totalCost,
        o1.quantity + o2.quantity
      )
    def empty = Order(0, 0)
  }
}

case class Order(totalCost: Double, quantity: Double)

object CatsMonoid extends App {
  println(Monoid[String].combine("Hi ", "there"))
  println(Semigroup[String].combine("Hi ", "there"))

  println(Monoid[Int].combine(32, 10))
  println(Monoid[Option[Int]].combine(Option(22), Option(23)))

  println("Hi " |+| "there" + Monoid[String].empty)
  println(1 |+| 2 |+| Monoid[Int].empty)

}

