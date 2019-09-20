---
title: Lua
date: 2019-06-29 17:55:26
categories: [Lua]
---

元表 Metatable
    改变table的行为,每个行为关联了对应的元方法.
    如: 定义两个table的相加操作.

数字:   字节, C语言整型, C语言size_t类型, Lua整型, Lua浮点型
        byte, uint32,   uint64,         int64,   float64   

字符串: 其实就是字节数组. 分为NULL,短字符串和长字符串
        NULL: 用0x00表示.
        长度小于0xFD的字符串: 先用一个字节记录长度+1, 然后是字节数组
        长度大于等于0xFE的字符串: 第一个字节是0xFF,后面跟一个size_t记录长度+1,最后是字节数组
列表: 先用cint记录列表长度,再紧接着存储n个列表元素