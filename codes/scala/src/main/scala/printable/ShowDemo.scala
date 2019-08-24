package printable

import cats.instances.int._
import cats.instances.string._
import cats.syntax.show._
import java.util.Date

import cats.Show

object appli extends App {
  println(123.show)
  println("abc".show)

  implicit val dateShow: Show[Date] =
    (date: Date) => s"${date.getTime}ms since the epoch."

  println(dateShow.show(new Date()))


}
