---
title: Java8查漏补缺
date: 2019-04-05 00:05:00
tags: [Java, 函数式编程]
---


1. 函数式接口里可以有继承自Object类的抽象方法.
    例:
        ```java
        @FunctionalInterface
        interface MyInterface {
            void test();
            String toString();
        }
        ```
    以上代码是合法的.
2. 函数式接口的实例可以通过 lambda表达式, 方法引用 或者 构造方法引用 创建.
        ```java
        MyInterface myInterface = () -> System.out.println("hello");
        System.out.println(myInterface.getClass());
        System.out.println(myInterface.getClass().getSuperclass());
        System.out.println(myInterface.getClass().getInterfaces()[0]);
        ```
        class sth.jdk8.Test2$$Lambda$2/1149319664
        class java.lang.Object
        interface sth.jdk8.MyInterface
        
    可以看到这个函数式接口实例的父类是Object,它实现了接口MyInterface

        通过方法引用创建函数式接口实例
        list.forEach(System.out::println);

3. 方法引用的四种形式:
     类名::实例方法 所对应lambda表达式的第一个参数就是这个调用方法的对象

4. Function<T, R>接口
    Represents a function that accepts one argument and produces a result.    
    抽象方法:     R apply(T t);
    例:
    ```java
        public String convert(int a, Function<Integer, String> function) {
             return function.apply(a);
        }
    ```
    默认方法: compose andThen
    静态方法: identity

    BiFunction<T, U, R>接口
    抽象方法: R apply(T t, U u);
    默认方法: <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after)

    重要建议: equals方法 把常量放在前面,变量放在后面. 可以避免变量为null时引发的空指针异常
