---
title: 神秘的Scala implicit
date: 2019-04-05 20:41:22
tags: [Scala]
---
# 神秘的Scala implicit

## 使用方式
- 隐式值
    编译器会在方法省略隐式参数的情况下去搜索作用域内的隐式值作为缺少参数
    例: 
        ```
        def person(implicit name: String) = name
        implicit val p = "ramble"
        person  // ramble
        ```
    
- 隐式视图
    1. 隐式转换为目标类型：把一种类型自动转换到另一种类型
    例: 
        ```
        def foo(msg: String) = println(msg)
        implicit def intToString(x: Int) = x.toString
        ```

    2. 使对象调用类中本不存在的方法
    例: 
        ```scala
        class SwingType {
            def wantLearned(sw: String) = println("兔子已经学会了" + sw)
        }

        object swimming {
            implicit def learningType(s: AnimalType) = new SwingType
        }

        class AnimalType

        object AnimalType extends App {
            import swimming._
            val rabbit = new AnimalType
            rabbit.wantLearned("蛙泳")  //兔子已经学会了蛙泳
        }
        ```
    
- 隐式类
    
