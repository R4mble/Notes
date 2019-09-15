const curry = f => a => b => f(a, b)
const unCurry = f => (a, b) => f(a)(b)
const compose = (f, g) => a => f(g(a))

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