---
title: JVM类加载机制
date: 2019-04-24 02:30:28
categories: [Java]
---
## 概述
    JVM类加载机制: 虚拟机把描述类的数据从Class文件加载到内存,并对数据进行校验,转换解析和初始化,最终形成可以被虚拟机直接使用的Java类型.

## 类加载时机
    类从加载到卸载的生命周期7个阶段: 
        加载,验证,准备,解析,初始化,使用和卸载. (验证,准备,解析 统称为 连接)
        解析可以在初始化之后开始.

    5种必须对类进行初始化的情况:
        1. 遇到new, getstatic, putstatic, invokestatic这4条字节码时.
        2. 使用reflect包方法对类进行反射调用时.
        3. 初始化一个类而它的父类还没有进行初始化, 需要先初始化它的父类
        4. 虚拟机启动时用户指定要执行的主类
        5. 动态语言支持

    不会触发初始化的引用--被动引用:
        1. 通过子类来引用父类中定义的静态字段,只会触发父类的初始化而不会触发子类的初始化.
        2. 通过数组定义来引用类,不会触发类的初始化
            package org.fenixsoft.classloading
                SuperClass[] sca = new SuperClass[10];
            类org.fenixsoft.classloading.SuperClass不会被初始化.
            但是Lorg.fenixsoft.classloading.SuperClass会被初始化.这是由虚拟机自动生成的,直接继承自java.lang.Object的子类
                创建动作由指令newarray触发.这个类代表了org.fenixsoft.classloading.SuperClass的一维数组,
                数组中应有的属性和方法(length属性和clone()方法)都实现在这个类里面
        3. 常量在编译阶段会存入调用类的常量池中,本质上没有直接引用到常量的类.

    对于接口初始化时,不要求其父接口全部完成初始化,只有真正使用到的时候才会初始化.
                                       

## 类加载过程
    加载
        1. 通过一个类的全限定名来获取定义此类的二进制字节流
        2. 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
        3. 在Java堆中生成一个代表这个类的java.lang.Class对象,作为方法区这些数据的访问入口
    验证

    准备

    解析

    初始化
## 类加载器
