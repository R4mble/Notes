package functordemo

import cats.Functor

/**
  * @author Wangyl
  * @date 2019/8/20
  */
sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree extends App {

  implicit val treeFunctor: Functor[Tree] =
    new Functor[Tree] {
      def map[A, B](tree: Tree[A])(func: A => B): Tree[B] =
        tree match {
          case Branch(left, right) =>
            Branch(map(left)(func), map(right)(func))
          case Leaf(value) =>
            Leaf(func(value))
        }
    }

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A] (value: A): Tree[A] =
    Leaf(value)

//  println(Branch(Leaf(10), Leaf(20)).map(_ * 2))

//  val v1 = Tree.leaf(100).map(_ * 2)
  println(v1)
//  val v2 = Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2)
  println(v2)

}