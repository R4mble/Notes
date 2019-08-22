package monadcats

import cats.Monad
import cats.instances.option._
import cats.instances.list._
import cats.syntax.applicative._
import cats.syntax.functor._
import cats.syntax.flatMap._

class MonadCats {

}

object MonadCats extends App {
  val opt1 = Monad[Option].pure(3)
  println(opt1)
  val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
  println(opt2)
  val opt3 = Monad[Option].map(opt2)(a => 100 * a)

  val list1 = Monad[List].pure(3)
  println(list1)
  val list2 = Monad[List].flatMap(List(1,2,3))(a => List(a, a*10))
  println(list2)
  val list3 = Monad[List].map(list2)(a => a + 123)
  println(list3)

  println(Monad[Option].flatMap(Option(1))(a => Option(a * 2)))
  println(Monad[List].flatMap(List(1,2,3))(a => List(a, a*10)))

  println(1.pure[Option])
  println(1.pure[List])

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(x => b.map(y => x*x + y*y))

  println(sumSquare(Option(3), Option(4)))
  println(sumSquare(List(1,2,3), List(4,5,6,7)))

  // 自动化flatMap和map
  def sumSquareFor[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x*x + y*y

  println(sumSquareFor(Option(3), Option(4)))
  println(sumSquareFor(List(1,2,3), List(4,5,6,7)))

  import cats.Id
  // 使用普通的值调用Monadic方法
  println(sumSquareFor(3: Id[Int], 4: Id[Int]))

  val a = Monad[Id].pure(3)
  println(a)
  val b = Monad[Id].flatMap(a)(_ + 1)
  println(b)

  val res = for {
    x <- a
    y <- b
  } yield x + y
  println(res)

  //不行, 没有flatMap
//  val res2 = for {
//    x <- 3
//    y <- 4
//  } yield x + y

//type Id[A] = A
  def pure[A](a: A): Id[A] = a
  def map[A, B](ida: Id[A])(f: A => B): Id[B] = f(ida)
  def flatMap[A, B](ida: Id[A])(f: A => Id[B]): Id[B] = f(ida)

  val either1: Either[String, Int] = Right(10)
  val either2: Either[String, Int] = Right(32)
   val res3 = for {
      a <- either1.right
      b <- either2.right
    } yield a + b
    println(res3)

  val res4 = for {
    a <- either1
    b <- either2
  } yield a + b
  println(res4)

  import cats.syntax.either._
  def countPositive(nums: List[Int]) =
    nums.foldLeft(0.asRight[String]) { (accumulator, num) =>
      if (num > 0) {
        accumulator.map(_ + 1)
      } else {
        Left("Negative. Stopping!")
      }
    }

  println(countPositive(List(1,2,3)))
  println(countPositive(List(1,-2,3)))

  println(Either.catchOnly[NumberFormatException]("foo".toInt))
  println(Either.catchNonFatal(sys.error("Badness")))
  println(Either.fromTry(scala.util.Try("foo".toInt)))
  println(Either.fromOption[String, Int](None, "Badness"))

  println("Error".asLeft[Int].getOrElse(0))
  println("Error".asLeft[Int].orElse(2.asRight[String]))

  println(-1.asRight[String].ensure("Must be non-negative!")(_ > 0))

  val res6 = "error".asLeft[Int].recover {
    case _: String => -1
  }
  println(res6)

  val res7 = "error".asLeft[Int].recoverWith {
    case _: String => Right(-1)
  }
  println(res7)

  println("foo".asLeft[Int].leftMap(_.reverse))
  println("foo".asRight[String].leftMap(_.reverse))
  println(6.asRight[String].bimap(_.reverse, _*7))
  println("foo".asRight[String].bimap(_.reverse, _*7))
  println("bar".asLeft[Int].bimap(_.reverse, _*7))

  println("123".asRight[String].swap)

}
