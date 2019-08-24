---
title: Scala实现并行框架  
date: 2019-04-05 09:21:14  
tags: [Scala]  
---
```scala
//接收一个未求值的A,返回一个并行结果(传名函数)
def unit[A](a: => A) : Par[A]

//从并行结果里抽取结果
def run[A](a: Par[A]) : A

//组合两个并行计算的结果
def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C) : Par[C]

//将Par分配到另一个独立的逻辑线程中运行
def fork[A](a: => Par[A]) : Par[A]

//派生组合子的一个例子
def unit[A](a: A) : Par[A]
def lazyUnit[A](a: => A) : Par[A] = fork(unit(a))

type Par[A] = ExecutorService => Future[A]
def run[A](s: ExecutorService)(a: Par[A]) : Future[A] = a(s)

object Par {
    def unit[A](a: A) : Par[A] = (es: ExecutorService) => UnitFuture(a)

    private case class UnitFuture[A](get: A) extends Future[A] {
        def isDone = true
        def get(timeout: Long, units: TimeUnit) = get
        def isCancelled = false
        def cancel(evenIfRunning: Boolean) : Boolean = false
    }

    def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C) : Par[C] =
        (es: ExecutorService) => {
            val af = a(es)
            val bf = b(es)
            UnitFuture(f(af.get, bf.getb))
        }

    def fork[A](a: => Par[A]) : Par[A] =
        es => es.submit(new Callable[A] {
            def call = a(es).get
        })
}
```