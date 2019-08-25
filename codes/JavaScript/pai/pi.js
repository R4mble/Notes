var Channel = /** @class */ (function () {
    function Channel(name) {
        this.name = name;
    }
    Channel.prototype.reduce = function () {
        if (!this.sendF || !this.receiveF)
            return;
        console.log("passing name " + this.c.name + " via channel " + this.name);
        var rnd = Math.random();
        if (rnd >= 0.5) {
            this.sendF();
            this.receiveF(this.c);
        }
        else {
            this.receiveF(this.c);
            this.sendF();
        }
    };
    Channel.prototype.send = function (c, f) {
        console.log(this.name + " sending " + c.name);
        this.c = c;
        this.sendF = f;
        this.reduce();
    };
    Channel.prototype.receive = function (f) {
        console.log(this.name + " receiving");
        this.receiveF = f;
        this.reduce();
    };
    return Channel;
}());
var channel = function (name) { return new Channel(name); };
var x = channel('x');
var y = channel('y');
var w = channel('w');
var a = channel('a');
/**
 * 当函数可以是一等公民时,谁都不知道一个普普通通的盒子里究竟藏了什么猫腻.
 *
 * 1. 说好的并发执行呢?
 * 答: js本身就是异步的,所以下面三个函数调用谁也不等谁,直接往后跑,算是并发执行了.
 *
 * 2. receive不是接收一个Channel吗,怎么接收一个函数了?
 * 答: receive接收一个 接收Channel z, 并使用z发送Channel a的函数.
 *
 * 3. send为什么有两个参数?
 * 答: 第一个参数是它要send的Channel, 第二个参数是send之后的动作.
 */
x.receive(function (z) { return z.send(a, function () { return console.log('term 1 over'); }); });
x.send(w, function () { return y.send(w, function () { return console.log('term 2 over'); }); });
y.receive(function (v) { return v.receive(function (u) { return (function () {
    return console.log("term 3 over, received " + u.name + " finally");
})(); }); });
