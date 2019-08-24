package printable
import java.util.Date

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val stringPrintable: Printable[String] =
    (value: String) => value

  implicit val intPrintable: Printable[Int] =
    (value: Int) => value.toString

  implicit val catPrintable: Printable[Cat] =
    (cat: Cat) => cat.name + " is a " + cat.age + " year-old " + cat.color + " cat."

  implicit val datePrintable: Printable[Date] =
    (date: Date) => date.getTime + "ms since the epoch."
}


import PrintableInstances._
object Printable {
  def format[A](value: A)(implicit p: Printable[A]): String =
    p.format(value)

  def print[A](value: A)(implicit p: Printable[A]): Unit =
    println(p.format(value))
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String =
      p.format(value)

    def print(implicit p: Printable[A]): Unit =
      println(p.format(value))
  }
}

final case class Cat(name: String, age: Int, color: String)

import PrintableSyntax._
object app extends App {
  val cat = Cat("miao", 5, "orange")
  Printable.print(cat)
  cat.print

  new Date().print
}
