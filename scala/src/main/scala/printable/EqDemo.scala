package printable

import cats.Eq
import cats.instances.int._
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

  val res = List(1, 2, 3).map(Option(_)).filter(item => item == 2)
  println(res)

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
}
