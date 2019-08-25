---
title: Ramda碎碎念
date: 2019-04-04 12:01:22
tags: [Ramda,函数式编程,柯西]
categories: [JavaScript]
---

prop基于path实现.
    prop = (p, obj) => path([p], obj)

    path一层层索引

        R.path(['a', 'b'], {a: {b: 2}}); //=> 2a
        R.prop('x', {x: 100}); //=> 100

descend = (f, a, b) => f(a) > f(b) ? -1 : f(a) < f(b) ? 1 : 0
类似地: ascend...

sort = (comparator, list) => Array.prototype.slice.call(list, 0).sort(comparator)
sort函数实现直接用的原生实现,不过返回了新list,而且curry了.

comparator :: a -> b -> Int

thrush combinator 接受一个值, 并将函数作用于其上.  applyTo = (x, f) => f(x)
    const t42 = R.applyTo(42);
    t42(R.identity); //=> 42
    t42(R.add(1)); //=> 43

keys 返回对象最外层key    


applySpec : 接受一个属性值为函数的对象,返回的函数使用传入的参数调用对象的每个属性位对应的函数，来生成相应属性的值。

    const getMetrics = R.applySpec({
        sum: (a,b,c) => a+b+c,
        nested: { mul: (a,b,c) => a*b*c }
    });
    print(getMetrics(2, 5, 4)) // { sum: 11, nested: { mul: 40 } }

    内部函数: mapValues = (f, obj) => 
                R.keys(obj).reduce((acc, key) => {
                    acc[key] = f(obj[key])
                    return acc
                }, {})
            是为了避免specs函数也包含map

    pluck 从列表内的每个对象元素中取出特定名称的属性，组成一个新的列表
        pluck = (p, list) => map(prop(p), list)

        var getAges = R.pluck('age');
        getAges([{name: 'fred', age: 29}, {name: 'wilma', age: 27}]); //=> [29, 27]

        R.pluck(0, [[1, 2], [3, 4]]);               //=> [1, 3]
        R.pluck('val', {a: {val: 3}, b: {val: 5}}); //=> {a: 3, b: 5}

addIndex: 没咋看懂
adjust: 将数组中指定索引处的值替换为经函数变换的值        
    R.adjust(1, R.toUpper, ['a', 'b', 'c', 'd']);      //=> ['a', 'B', 'c', 'd']
    R.adjust(-1, R.toUpper, ['a', 'b', 'c', 'd']);     //=> ['a', 'b', 'c', 'D']

    实现: 统一索引,利用_concat新建list,将索引处值替换.

    