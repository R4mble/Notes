---
title: Ramda中的lens源码解读
date: 2019-04-03 01:12:57
tags: [函数式编程,Ramda,lens]
categories: [探索Ramda]
---
先上源码:
```JavaScript
 * @typedefn Lens s a = Functor f => (a -> f a) -> s -> f s
 * @sig (s -> a) -> ((a, s) -> s) -> Lens s a
 * @param {Function} getter
 * @param {Function} setter
 * @return {Lens}
 * @see R.view, R.set, R.over, R.lensIndex, R.lensProp
 * @example
 *
 *      const xLens = R.lens(R.prop('x'), R.assoc('x'));
 *
 *      R.view(xLens, {x: 1, y: 2});            //=> 1
 *      R.set(xLens, 4, {x: 1, y: 2});          //=> {x: 4, y: 2}
 *      R.over(xLens, R.negate, {x: 1, y: 2});  //=> {x: -1, y: 2}
 */
var lens = _curry2(function lens(getter, setter) {
  return function(toFunctorFn) {
    return function(target) {
      return map(
        function(focus) {
          return setter(focus, target);
        },
        toFunctorFn(getter(target))
      );
    };
  };
});
```
不得不说这文档写的真棒,我好了.
使用代换模型人肉解析:
```JavaScript
const xLens = R.lens(R.prop('x'), R.assoc('x'));
当执行完这一行时,
xLens = function(toFunctorFn) {
    return function(target) {
      return map(
        function(focus) {
          return R.assoc('x')(focus, target);
        },
        toFunctorFn(R.prop('x')(target))
      );
    };
  };
```

同时也看下view的代码:
```JavaScript
 * @typedefn Lens s a = Functor f => (a -> f a) -> s -> f s
 * @sig Lens s a -> s -> a
 * @param {Lens} lens
 * @param {*} x
 * @return {*}
 * @see R.prop, R.lensIndex, R.lensProp
 * @example
 *
 *      const xLens = R.lensProp('x');
 *
 *      R.view(xLens, {x: 1, y: 2});  //=> 1
 *      R.view(xLens, {x: 4, y: 2});  //=> 4
 */
var view = _curry2(function view(lens, x) {
  // Using `Const` effectively ignores the setter function of the `lens`,
  // leaving the value returned by the getter function unmodified.
  return lens(Const)(x).value;
});
```

```JavaScript
// `Const` is a functor that effectively ignores the function given to `map`.
var Const = function(x) {
  return {value: x, 'fantasy-land/map': function() { return this; }};
};
```JavaScript

 R.view(xLens, {x: 1, y: 2});

 function(toFunctorFn) {
    return function(target) {
      return map(
        function(focus) {
          return R.assoc('x')(focus, target);
        },
        toFunctorFn(R.prop('x')(target))
      );
    };
  }; (Const)(x).vale

  map(function(focus) {
          return R.assoc('x')(focus, {x: 1, y: 2});
        },
        Const(R.prop('x')({x: 1, y: 2})))

  map(function(focus) {
          return R.assoc('x')(focus, {x: 1, y: 2});
        },
        Const(1))

    1
```
Ramda里面有很多地方用到了这个函数_dispatchable