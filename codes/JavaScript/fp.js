function Just(val) {
    this.value = val
}
// map :: fa -> (a -> b) -> fb
Just.prototype.map = function (f) {
    return new Just(f(this.value))
};
Just.of = function(val) {
    return new Just(val)
};
// ap :: f(a -> b) -> f a -> f b
Just.prototype.ap = function (container) {
    return container.map(this.value);
};
Just.prototype.flat = function() {
    return this.value;
};
Just.prototype.flatMap = function(f) {
    return this.map(f).flat();
};

const f = x => x + 'b';
const a = 2;
const b = f(a);
const fa = Just.of(a);
const fb = fa.map(f);
// console.log(fb);

//Applicative Functor 
const fplus2 = Just.of(x => x + 2);
// console.log(fplus2.ap(Just.of(3)));

function Nothing() {};
Nothing.prototype.map = function() {
    return this;
}
Nothing.prototype.flatMap = Nothing.prototype.map;

const nothing = new Nothing;

// Monad让我们可以把一个容器中的值传入一个接收值的函数中,并返回同样的容器.
const monad = Just.of(2)
                .flatMap(x => {
                    if (x)
                        // 装箱装箱再拆箱
                        return Just.of(x)
                    else
                        return nothing
                })
                .map(x => x + 1)
                .map(x => x * 2);

// console.log(monad);

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

//Either函子
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


var IO = function(f) {
    this.unsafeIO = f
}
// value constructor
IO.of = function(x) {
    return new IO(() => x)
}
IO.prototype.map = function(f) {
    return new IO(compose(f, this.unsafeIO))
}

const compose = (f, g) => x => f(g(x))
const compose3 = (f, g, h) => x => f(g(h(x)))
const prop = p => obj => obj[p]
const head = lst => lst[0]
const last = lst =>lst[lst.length - 1]
const split = sp => word =>word.split(sp)
const filter = fn => list => {
    var idx = 0;
    var len = list.length;
    var result = [];
    while (idx < len) {
        if (fn(list[idx])) {
            result[result.length] = list[idx];
        }
        idx += 1;
    }
    return result;
}
const map = (f, funtor) => funtor.map(f)
const maplst = f => lst => lst.map(f)
const eq = k => v => k == v


var io_window =  new IO(() => window);
var width = io_window.map(win => win.innerWidth);
console.log(width.unsafeIO());

var url = io_window.map(prop('location'))
                    .map(prop('href'));

console.log(url.map(split('/')).unsafeIO())                    

var $ = function(selector) {
    return new IO(() => document.querySelectorAll(selector))
}       
var content = $('#myDiv').map(head).map(div => div.innerText)
console.log(content.unsafeIO())

var toPairs = compose(maplst(split('=')), split('&'))
var params = compose3(toPairs, last, split('?'))

var findParam = key => map(compose3(MayBe.of, 
                                    filter(compose(eq(key), head)), 
                                    params),
                           url);

var param = findParam('searchTerm').unsafeIO()                 
console.log(param)



