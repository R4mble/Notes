---
title: JavaScript里的Monad
date: 2019-04-05 20:41:22
tags: [JavaScript,Monad]
---
```javascript
const f = x => x + 'b';
const a = 2;
const b = f(a);
console.log(b);

function Just(val) {
    this.value = val
}

Just.prototype.map = function (f) {
    return new Just(f(this.value))
};
Just.of = function(val) {
    return new Just(val)
};
Just.prototype.ap = function (container) {
    return container.map(this.value);
};

const fa = new Just(a);
//map over, fmap :: (a -> b) -> fa -> fb, a到b的映射接受a的Functor后返回b的Functor
const fb = fa.map(f);
console.log(fb);

//Applicative Functor   f (a -> b) -> f a -> f b, a到b映射的容器接受a的容器返回b的容器
const fplus2 = Just.of(x => x + 2);
console.log(fplus2.ap(Just.of(3)));

function Nothing() {};
Nothing.prototype.map = function() {
    return this;
}
const nothing = new Nothing;

Just.prototype.flat = function() {
    return this.value;
};
Just.prototype.flatMap = function(f) {
    return this.map(f).flat();
};
Nothing.prototype.flatMap = Nothing.prototype.map;

//Monad让我们可以把一个容器中的值传入一个接收值的函数中,并返回同样的容器.
var monad = Just.of(undefined)
                .flatMap(x => {
                    if (x)
                        return Just.of(x.value)
                    else
                        return nothing
                })
                .map(x => x + 1)
                .map(x => 2 / x);

console.log(monad);
console.log(Just.of(undefined));

// 函子是一个普通对象,它实现了map函数,在遍历每个对象值的时候生成一个新的对象
// 函子是持有值的容器
const Container = function(val) {
    this.value = val
};

Container.of = function (value) {
    return new Container(value)
};

Container.prototype.map = function(fn) {
    return Container.of(fn(this.value))
};

let double = (x) => 2 * x;
Container.of(3).map(double);


//MayBe函子
const MayBe = function (val) {
    this.value = val
};

MayBe.of = function (val) {
    return new MayBe(val)
};

MayBe.prototype.isNothing = function () {
    return (this.value === null || this.value === undefined)
};

MayBe.prototype.map = function (fn) {
    return this.isNothing() ? MayBe.of(null) : MayBe.of(fn(this.value))
};

//MayBe用例
MayBe.of("string").map(x => x.toUpperCase());
MayBe.of("null").map(x => x.toUpperCase());

MayBe.of("George")
    .map(x => x.toUpperCase())
    .map(x => "Mr. " + x);

//第二个map函数仍然会被调用
MayBe.of("George")
    .map(x => null)
    .map(x => "Mr. " + x);

//Either函子
const Nothing = function (val) {
    this.value = val
};
Nothing.of = function (val) {
    return new Nothing(val)
};
Nothing.prototype.map = function (f) {
    return this;
};
const Some = function (val) {
    this.value = val;
};

Some.of = function (val) {
    return new Some(val)
};

Some.prototype.map = function (fn) {
    return Some.of(fn(this.value))
};

const Either = {
    Some: Some,
    Nothing: Nothing
};


```