/**
  * @author Wangyl
  * @date 2019/8/26
  */
class Begin {

}

case class Employee(name: String, number: Int, manager: Boolean)
case class IceCream(name: String, numCherries: Int, inCone: Boolean)

import shapeless._

object Begin extends App {
  val genericEmployee = Generic[Employee].to(Employee("Dave", 123, false))
  println(genericEmployee)
  val genericIceCream = Generic[IceCream].to(IceCream("Sundae", 1, false))
  println(genericIceCream)

  def genericCsv(gen: String :: Int :: Boolean :: HNil): List[String] =
    List(gen(0), gen(1).toString, gen(2).toString)

  println(genericCsv(genericEmployee))
  println(genericCsv(genericIceCream))
}

