---
title: Java语法糖
date: 2019-05-14 01:35:59
categories: [Java]
---
# Switch对String的支持
语法糖:
```java
    String str = "world";
    switch (str) {
    case "hello":
        System.out.println("hello");
        break;
    case "world":
        System.out.println("world");
        break;
    default:
        break;
    }

```
实际上:
```java
    String str = "world";
    String s;
    switch((s = str).hashCode())
    {
    default:
        break;
    case 99162322:
        if(s.equals("hello"))
            System.out.println("hello");
        break;
    case 113318802:
        if(s.equals("world"))
            System.out.println("world");
        break;
    }
```
先比较hashCode，再使用equals进行安全检查，防止哈希碰撞

# 泛型
Code sharing方式处理泛型.为每个泛型类型创建唯一的字节码表示,并且将该泛型类型的实例
都映射到这个惟一的字节码表示上.
```java
Map<String, String> map = new HashMap<String, String>();  
map.put("name", "hollis");  
map.put("wechat", "Hollis");  
map.put("blog", "www.hollischuang.com");  
```

```java
Map map = new HashMap();  
map.put("name", "hollis");  
map.put("wechat", "Hollis");  
map.put("blog", "www.hollischuang.com");  
```
类型擦除的主要过程如下： 
1.将所有的泛型参数用其最左边界（最顶级的父类型）类型替换。 
2.移除所有的类型参数。

# 变长参数
可变参数在被使用的时候，会创建一个数组，把参数值全部放到这个数组当中，然后再把这个数组作为参数传递到被调用的方法中。

# 枚举
使用enmu来定义一个枚举类型的时候，编译器会自动帮我们创建一个final类型的类继承Enum类，所以枚举类型不能被继承。
条件编译
# 自动拆装箱
```java
    //语法糖
    Integer i = 10;
    int n = i;
    //实际上
    Integer i = Integer.valueOf(10);
    int n = i.intValue();
```
装箱过程是通过调用包装器的valueOf方法实现的，而拆箱过程是通过调用包装器的 xxxValue方法实现的。

# 条件编译
根据if判断条件的真假，编译器直接把分支为false的代码块消除。

# 数值字面量
在java 7中，数值字面量，不管是整数还是浮点数，都允许在数字之间插入任意多个下划线。这些下划线不会对字面量的数值产生影响，目的就是方便阅读。

# for-each
使用了普通的for循环和迭代器
```java
    for (String s : strList) {
        System.out.println(s);
    }
    
   for(Iterator iterator = strList.iterator(); iterator.hasNext(); System.out.println(s))
        s = (String)iterator.next();
```