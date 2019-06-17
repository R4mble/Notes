---
title: Ramda里的柯西化实现
date: 2019-04-04 02:01:22
tags: [Ramda,函数式编程,柯西]
categories: [探索Ramda]
---
先说R.__ (两个下划线)
```javascript
var __ = {'@@functional/placeholder': true};

 *      const greet = R.replace('{name}', R.__, 'Hello, {name}!');
 *      greet('Alice'); //=> 'Hello, Alice!'
```
R.__是一个含有@@functional/placeholder属性的对象, 那么它是怎么起到占位符作用的呢?

看下这个函数
```javascript
function _isPlaceholder(a) {
  return a != null &&
         typeof a === 'object' &&
         a['@@functional/placeholder'] === true;
}
```
这个函数的作用就是检查一个对象的@@functional/placeholder属性是否为true.

第一个curry函数:
```javascript
function _curry1(fn) {
  return function f1(a) {
    if (arguments.length === 0 || _isPlaceholder(a)) {
      return f1;
    } else {
      return fn.apply(this, arguments);
    }
  };
}
```