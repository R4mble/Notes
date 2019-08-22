package monoiddemo

/**
  * @author Wangyl
  * @date 2019/8/20
  */
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] =
    monoid

  implicit val booleanAndMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      override def empty: Boolean = true
      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }

  implicit val booleanOrMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      override def empty: Boolean = false
      override def combine(x: Boolean, y: Boolean): Boolean = x || y
    }

  implicit val booleanEitherMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      override def empty: Boolean = false
      override def combine(x: Boolean, y: Boolean): Boolean =
        (x && !y) || (!x && y)
    }

  implicit val booleanXnorMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      override def empty: Boolean = true
      override def combine(x: Boolean, y: Boolean): Boolean =
        (!x || y) && (x || !y)
    }

  // 合集
  // a method rather than a value, so we can accept the type parameter A
  implicit def setUnionMonoid[A]: Monoid[Set[A]] =
    new Monoid[Set[A]] {
      override def empty: Set[A] = Set.empty
      override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
    }

  // 交集
  implicit def setIntersectionSemigroup[A]: Semigroup[Set[A]] =
    (a: Set[A], b: Set[A]) => a intersect b

  // 补集和差集不能构成semigroup

  // 互相差集的合集
  implicit def symDiffMonoid[A]: Monoid[Set[A]] =
    new Monoid[Set[A]] {
      override def empty: Set[A] = Set.empty
      override def combine(x: Set[A], y: Set[A]): Set[A] =
        (x diff y) union (y diff x)
    }

}

object main extends App {
//  val intSetMonoid = Monoid[Set[Int]]
//  val strSetMonoid = Monoid[Set[String]]
//
//  println(intSetMonoid.combine(Set(1,2), Set(2,3)))
//  println(strSetMonoid.combine(Set("A", "B"), Set("B", "C")))

}

