---
title: lambda演算入门
date: 2019-04-03 14:31:59
tags: [函数式编程]
---
来写一写lambda演算里的东西,主要是用代换模型人肉parse.
## 求值
    一个"λ"一个".", 极简函数写法,
    比function,=>,->,lambda() (),def [] {}等等各种写法不知道高到哪里去了.
    例子:
        (λx.(λy.x))a 求值结果为λy.a 
        就是把a带进去,返回一个"输入y返回a"的函数.

## 布尔逻辑    
    T 表示为：λx.λy.x      它接收两个参数x和y,返回x, 也就是说它返回前面的那个参数
    F 表示为：λx.λy.y      它接收两个参数x和y,返回y, 也就是说它返回后面的那个参数
    这样一来, if函数就是λbtf, 等价于λb.λt.λf.btf
             if函数接收三个参数,分别是判断条件b,
                                    当b为True时: (λx.λy.x)(t)(f)  返回的语句t 
                                    和b为False时: (λx.λy.y)(t)(f) 返回的语句f

             布尔逻辑运算符与或非分别是:
             与: λab.IFabF  接收两个参数a和b, 如果a是True,那就返回前一个也就是b,与的结果和b的取值一样
                                             如果a是False,那就返回后一个就是False
             或: λab.IFaTb  接收两个参数a和b, 如果a是True,那就返回前一个也就是True
                                             如果a是False,那就返回后一个就是b,或的结果和b的取值一样
             非: λa.IFaFT   接收一个参数a, 如果a是True,那就返回前一个也就是False
                                          如果a是False,那就返回后一个也就是True

## 数
    0=λf.λx.x
    1=λf.λx.fx
    2=λf.λx.f(fx)
    3=λf.λx.f(f(fx))
    后继函数S=λn.λf.λx.f((nf)x)
    加法: ADD = λab.(aS)b
          举例: 0和0相加:  ((λf.λx.x)(λn.λf.λx.f((nf)x)))(λf.λx.x)
                         = (λx.x)(λf.λx.x)
                         = λf.λx.x
                         = 0
                1和2相加: (λf.λx.fx)(λn.λf.λx.f((nf)x))(λf.λx.f(fx))
                         = (λn.λf.λx.f((nf)x))(λf.λx.f(fx))
                         = λf.λx.f(((λf.λx.f(fx))f)x)
                         = λf.λx.f((λx.f(fx))x)
                         = λf.λx.f(f(fx))
                         = 3
