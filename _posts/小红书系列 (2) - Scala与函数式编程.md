---
title: 小红书系列 (2) - Scala与函数式编程
date: 2019-09-14 17:42:45
categories: [Scala]
---
## Scala与Haskell
> Scala is a gateway drug to Haskell.

读过<< Learn You a Haskell for great good>>这本书, Haskell确实优雅, 但同时也让人感觉过于高贵冷艳, 还是Scala读起来更舒服.

## Scala函数式编程Demo
我感觉有些文章反复出现在视野, 说起函数式编程就是map, 说起范畴论就是compose.甚至还有人用几个带点函数式意味的函数后就瞧不起别人, 感觉别人都看不懂, 真是naive!  =_=

### Demo1: 阶乘
这个样子的代码我写过太多遍了, 尤其是在SICP的练习里. 
同时这个例子也涉及到尾递归优化的问题, 因为递归太多, 容易爆栈, 所以要递归转迭代.
加上这个注解@annotation.tailrec, 也可以让编译器来做尾调用.

```scala
def factorial(n: Int): Int = {
  def go(n: Int, acc: Int): Int =
    if (n <= 0) acc
    else go(n-1, n*acc)

  go(n, 1)
}
```

### Demo2: 斐波那契数列
写到吐的斐波那契数列, 先来一个递归的: 
```scala
def fib(n: Int): Int = 
  if (n < 2) n
  else fib(n-1) + fib(n-2)
```

再来个迭代的: 
```scala
def fib(n: Int): Int = {
  def go(n: Int, prev: Int, cur: Int): Int =
    if (n == 0) prev
    else go(n-1, cur, prev + cur)

  go(n, 0, 1)
}
```
形象地说, 递归是随着一轮轮计算越变越长, 而迭代一直不变长.

### Demo3: 在数组中找到第一个匹配的元素的索引
```scala
def findFirst[A](as: Array[A], p: A => Boolean): Int = {
  def go(n: Int): Int =
    if (n >= as.length) -1
    else if (p(as(n))) n
    else go(n+1)
  
  go(0)
}
```

### Demo4: 检查数组是否按照给定的比较函数排序
上次看到群里有人问这个函数怎么写, 我一时之间还想不出来该怎么写. 这种玩具代码, 真是容易忘记啊.
```scala
def isSorted[A](as: Array[A], ordered: (A,A) => Boolean): Boolean = {
  def go(n: Int): Boolean =
    if (n+1 >= as.length) true
    else if (!ordered(as[n], as[n+1])) false
    else go(n+1)

  go(0)
}
```

### Demo5: 函数式三连: 柯西, 反柯西和组合
```Scala
def curry[A,B,C](f: (A,B) => C): A => (B => C) =
  a => b => f(a, b)

def unCurry[A,B,C](f: A => B => C): (A, B) => C =
  (a, b) => f(a)(b)

def compose[A,B,C](f: B => C, g: A => B): A => C =
  a => f(g(a))
```

用JavaScript来写写看:   
```JavaScript
const curry = f => a => b => f(a, b)
const unCurry = f => (a, b) => f(a)(b)
const compose = (f, g) => a => f(g(a))


// 一些测试的例子
const print = console.log

const add = (a, b) => a + b
print(add(3, 2))

const curryAdd = curry(add)
print(curryAdd(3)(2))

const unCurryAdd = unCurry(curryAdd)
print(unCurryAdd(3,2))

const mul3 = a => a * 3
const add2 = a => a + 2
const composeAddMul = compose(add2, mul3)
print(composeAddMul(3))

const composeMulAdd = compose(mul3, add2)
print(composeMulAdd(3))
```

可以看出JavaScript算的上是键盘友好型FP选手了.