---
title: PostgreSQL
date: 2019-04-04 12:01:22
tags: [PostgreSQL]
categories: [SQL]
---

## 特点
- multi-version concurrency control (MVCC), Oracle中的快照隔离
- 面向对象, 可以添加自定义函数.
- 可拓展, 可以自定义数据类型,索引类型. 

## PostgreSQL与MySQL的对比:
  1. MySQL多表连接查询只支持Nest Loop,不支持hash join和sort merge join,子查询性能较低,不支持sequence
  2. 性能优化工具和度量信息不足, 异步复制,无法通过主从做到数据零丢失
  3. 在线操作功能弱.

## 数据类型
- 基本数据类型: Integer, Numeric, String, Boolean, 二进制bytea, 
- 字符型: varchar(n), char(n), text
- 位串类型: bit(n), bit varying(n)
- 枚举类型: enum
- 网络地址类型: cidr, inet, macaddr
- 结构型: Data/Time, Array, Range, UUID
- 文档型: JSON, XML, K-V
- 几何型: 点, 线, 圈, 多边形
- 定制型: 组合, 定制类型

## 数据完整性
- UNIQUE, NOT NULL
- 主键
- 外键
- 排除约束
- Explicit Locks
- Advisory Locks

## 常用操作
- select 连接两列合并为一列
> select first_name  || ' ' ||  last_name as full_name, email from customer;
- order by时升序NULL在最后，降序NULL在最前
- distinct bcolor, fcolor 这样使用distinct时会把这两列作为整体进行去重
- > select distinct on (bcolor) bcolor, fcolor from t1 order by bcolor ,fcolor;     
    
    先按照bcolor, fcolor对行进行排序，每一组重复的bcolor中选取第一个所在的行。
- >limit n offset m 

    跳过m个，选取n个. 注意m数量不要太大  
    
   fetch是postgresql对sql标准的实现，和limit offset功能相同
  > OFFSET start { ROW | ROWS } FETCH { FIRST | NEXT } [ row_count ] { ROW | ROWS } ONLY        
 - where in 既可以接列表，也可以接一个子查询。
 - like匹配符： % 匹配任意个字符， _ 匹配单个字符
 - 和null做 = 比较，永远为false， 因此使用 is null 和 is not null

- 找到表a中有的b中没有的行: 
    > basket_a a LEFT JOIN basket_b b ON a.fruit = b.fruit WHERE b.id IS NULL;  
- 找到表a和表b中各自独特的行：
    > basket_a a FULL JOIN basket_b b ON a.fruit = b.fruit WHERE a.id IS NULL OR b.id IS NULL;
- join 等价于 inner join
- self-join 查询有层级体系的数据，同一张表中比较行。
- json与jsonb：
  - json是对输入文本的完整拷贝，保留输入的空格，重复键和顺序。处理函数必须在每个执行上重新解析。存储快，读取慢。
  - jsonb是解析输入后保存的二进制，在解析时会删除不必要的空格和重复键，顺序也会不同，使用时不用再次解析。支持索引。存储慢，读取快。


