---
title: The Little系列(1) - A Little Java, A Few Patterns
date: 2019-09-17 17:40:56
tags: [PL]
---
> 前言: 此书教你怎么在面向对象的语言中使用函数式设计
# Modern Toys
Java, 2000年的新玩具, 2019年的巨无霸.
值得一提的是嵌套构造器, 有点函数式的味道.
# Methods to Our Madness

有意思的来了: 

```Java
// 烤串儿
abstract class Shish {
    // 串儿里面是不是只有洋葱
    abstract boolean onlyOnions();
    // 串儿是不是纯素食
    abstract boolean isVegetarian();
}

// 洋葱
class Onion extends Shish {
    Shish s;

    Onion(Shish s) {
        this.s = s;
    }

    boolean onlyOnions() {
        return s.onlyOnions();
    }

    boolean isVegetarian() {
        return s.isVegetarian();
    }
}

// 土豆
class Tomato extends Shish {
    Shish s;

    Tomato(Shish s) {
        this.s = s;
    }

    boolean onlyOnions() {
        return false;
    }

    boolean isVegetarian() {
        return s.isVegetarian();
    }
}

// 羊肉
class Lamb extends Shish {
    Shish s;

    Lamb(Shish s) {
        this.s = s;
    }

    boolean onlyOnions() {
        return false;
    }

    boolean isVegetarian() {
        return false;
    }
}

// 串儿
class Skewer extends Shish {
    boolean onlyOnions() {
        return true;
    }

    boolean isVegetarian() {
        return true;
    }
}
```

核心层: Skewer, 它的onlyOnions方法相当于初始值: true
其他附加层: onlyOnions方法返回false, 因为加了它们就不是onlyOnions了
Onion: 返回它的构造器传入的Shish的onlyOnions方法值. 委托给别人来决定.

另一个例子: Kebab和Shish这个很相似,除了提高词汇量不值一提.
最后一个例子: Point也是讲抽象类的语法.


# What's New
