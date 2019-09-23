---
title: JavaScript教程
date: 2019-04-04 20:41:22
categories: [JavaScript]
---
基本语法
    变量
        声明和赋值
            var a = 1;  
            当只声明了变量,而没有赋值时 为 undefined

        变量没有类型,只有值有类型
            var a = 42;
            typeof a; // "number"

            a = true;
            typeof a; // "boolean"

            typeof typeof 42; // "string"

        变量提升
            JavaScript引擎会先解析代码,获取所有被声明的变量.
                console.log(a);
                var a = 1;
                //执行时相当于先声明了a,但是没有赋值,会打印出undefined

    标识符
        第一个字符可以是任意Unicode字母,不能是数字, 包括 $ 和 _

    区块
        对var来说,区块不构成单独的作用域
        {
            var a = 1;
        }
        a // 1

    逻辑控制
        if else switch 三目运算符 while for do...while break continue 同 Java

    标签
        通常和break,continue配合

        top:
            for (var i=0; i<3; i++) {
                for (var j=0; j<3; j++) {
                    if (i===1 && j===1) break top;
                }
            }
        满足条件跳出双层循环. 如果不使用标签则只能跳出内层循环.

        

数据类型
    number      整数和小数
    string
    boolean
    undefined
    null
    object
    symbol

    typeof运算符
        typeof 123 // "number"
        typeof "123" // "string"
        typeof false // "boolean"
        
        函数 返回 function
        function f() {}
        typeof f // "function"

        未定义变量 返回 undefined, 可以用来检测没有声明的变量
        typeof undefined === "undefined"

        typeof window // "object"
        typeof {} // "object"
        typeof [] // "object"

        var o = {};
        var a = [];

        区分数组和对象
        o instanceof Array //false
        a instanceof Array //true


        typeof null === "object" //这个是存在了20年的bug,应该返回null

        检测null:
            val a = null;
            (!a && typeof a === "object") //true, null是假值, undefined也是假值

        undefined == null //true

        Number(null) //0
        5 + null //5

        Number(undefined) //NaN
        5 + undefined // NaN

        null表示空值
        undefined表示未定义
            1)变量声明了但没有赋值
            2)调用函数时该提供的参数没有提供
            3)对象没有该属性
            4)函数没有返回值,默认返回 undefined


    布尔值
        以下为false, 其他为true
        undefined
        null
        false
        0
        NaN
        "" 或 ''

        空数组和空对象都是true

    数值
        所有数值以64位浮点数形式存储,也就是说这个语言根本没有整数
            1 === 1.0  true

        浮点数不精确,所以
            0.1 + 0.2 === 0.3  //false

            0.3 / 0.1   //2.999999999

            (0.3 - 0.2) === (0.2 - 0.1)  //false

        64个二进制位: 第1位 符号位,
                     第2位到第12位(共11位) 指数部分, 
                     第13位到第64位(共52位) 小数部分.

函数作用域:
     其它类C语言,一对花括号封闭的代码块就是一个作用域, JavaScript的作用域
     是通过函数来定义的.
     静态作用域,词法作用域,作用域的嵌套关系可以在语法分析时确定,而不必等到
     运行时确定.

闭包:
    由函数及其封闭的自由变量组成的集合体.
    特性: 当一个函数返回它内部定义的一个函数时,就产生了闭包,闭包不但包括被返回的函数,
          还包括这个函数的定义环境.

    用途: 实现嵌套的回调函数
            由于闭包,即使外层函数已经执行完毕,其作用域内申请的变量也不会释放,因为里层
            的函数还有可能引用到这些变量.
         隐藏对象的细节
            

        




            

        


