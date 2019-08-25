---
title: Redis跳表  
date: 2019-08-25 09:21:14  
tags: [Redis]  
categories: [Redis]
---
SkipList 有序数据结构
    在每个节点维持多个指向其他节点的指针.
    平均O(logN),最坏O(N)复杂度的节点查找,可以通过顺序性操作来
        批量处理节点.
    当有序集合包含的元素数量较多, 或有序集合中元素的member是比较长的字符串时,
    Redis使用跳表来作为有序集合键的底层实现.

跳表在Redis的两个应用: 有序集合键, 集群节点中做内部数据结构


zskiplist 
    header: 指向跳表的表头节点
    tail: 指向跳表的表尾节点
    level: 记录目前跳表内,层数最大的那个节点的层数(表头节点的层数不计算在内)
    length: 跳表目前包含节点的数量(表头节点不计算在内)

zskiplistNode
    level: L1代表第一层, L2代表第二层.