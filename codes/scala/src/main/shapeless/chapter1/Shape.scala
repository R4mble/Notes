package chapter1

/**
  * @author Wangyl
  * @date 2019/8/26
  */
sealed trait Shape
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Circle(radius: Double) extends Shape

object Shape extends App {
  val rect: Shape = Rectangle(3.0, 4.0)
  val circ: Shape = Circle(1.0)

  def area(shape: Shape): Double =
    shape match {
      case Rectangle(width, height) => width * height
      case Circle(radius) => math.Pi * radius * radius
    }

  println(area(rect))
  println(area(circ))

  type Rectangle2 = (Double, Double)
  type Circle2 = Double
  type Shape2 = Either[Rectangle2, Circle2]

  val rect2: Shape2 = Left(3.0, 4.0)
  val circ2: Shape2 = Right(1.0)

  def area2(shape2: Shape2): Double =
    shape2 match {
      case Left((w, h)) => w * h
      case Right(r) => math.Pi * r * r
    }
  println(area2(rect2))
  println(area2(circ2))

}

