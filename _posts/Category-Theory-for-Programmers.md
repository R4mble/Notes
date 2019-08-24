---
title: Category Theory for Programmers
date: 2019-04-27 18:02:47
tags: [读书笔记]
categories: [范畴论]
---
# Category: The Essence of Composition
## 简介: 
> A category consists of objects and arrows  that go between them.
    范畴与组合互为本质:
        The essence of a category is composition.
        The essence of composition is a category.
---        
## 组合的性质:
        1. Associativity
            (伏笔:  Associativity is pretty obvious when dealing with functions, 
                    but it may be not as obvious in other categories.)
        2. Identity

    组合是编程的本质
---
    Challenges 
      实现identity和compose
```java
public interface Function<T, R> {
    R apply(T t);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
```
    测试
    
```java
public class IdentityTest {

    public int compute(int a, Function<Integer, Integer> f1, Function<Integer, Integer> f2, Function<Integer, Integer> id) {
        return f1.compose(id).compose(f2).apply(a);
    }

    public int compute2(int a, Function<Integer, Integer> f1, Function<Integer, Integer> f2, Function<Integer, Integer> id) {
        return f1.compose(f2).compose(id).apply(a);
    }

    public static void main(String[] args) {
        int res = new IdentityTest().compute(1, x -> x * 2, x -> x + 2, Function.identity());
        int res2 = new IdentityTest().compute2(1, x -> x * 2, x -> x + 2, Function.identity());
        System.out.println(res);
        System.out.println(res2);
    }
}
```
用JavaScript来实现
```javascript
const identity = x => x
const compose = (f, g) => x => f(g(x))
```
测试
```javascript
const add5 = x => x + 5
const multi2 = x => x * 2

console.log(compose(identity, compose(add5, multi2))(5))
console.log(compose(add5, compose(identity, multi2))(5))
```


# Types and Functions

# Categories Great and Small

# Kleisli Categories

# Products and Coproducts

# Simple Algebraic Data Types

# Functors

# Functoriality

# Function Types

# Natural Transformations

# Declarative Programming

# Limits and colimits

# Free Monoids

# Representable Functors

# The Yoneda Lemma

# Yoneda Embedding

# Morphisms

# Adjunctions

# Free/Forgetful Adjunctions

# Monads: Programmer's Definition

# Monads and Effects
