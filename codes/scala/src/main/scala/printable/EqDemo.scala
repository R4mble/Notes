package printable

import cats.Eq
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.syntax.eq._
import cats.syntax.option._
import java.util.Date
import cats.instances.long._

class EqDemo {

}

object EqDemo extends App {

  val eqInt = Eq[Int]
//  eqInt.eqv(123, "123")

  123 === 123
  123 =!= 234
//  123 === "123"

  println((Some(1): Option[Int]) === None)

  println(1.some === none)
  println(1.some =!= none)

//  val res = List(1, 2, 3).map(Option(_)).filter(item => item == 2)
//  println(res)

  implicit val dateEq: Eq[Date] =
    Eq.instance[Date] {
      (date1, date2) =>
        date1.getTime === date2.getTime
    }

  val t1 = new Date()
  Thread.sleep(100)
  val t2 = new Date()

  println(new Date() === new Date())
  println(t1 === t2)


  val cat1 = Cat("Gar", 3, "black")
  val cat2 = Cat("Hea", 4, "white")

  implicit val catEqual: Eq[Cat] =
    Eq.instance[Cat] {
      (cat1, cat2) =>
        cat1.name === cat2.name &&
        cat1.age === cat2.age &&
        cat1.color === cat2.color
    }

  println(cat1 === cat2)
  println(cat1 =!= cat2)

  val optionCat1 = Option(cat1)
  val optionCat2 = Option.empty[Cat]

  println(optionCat1 === optionCat2)
  println(optionCat1 =!= optionCat2)
}
