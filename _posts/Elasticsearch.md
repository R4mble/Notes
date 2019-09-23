---
title: Elasticsearch
date: 2019-04-27 23:33:37
tags: [读书笔记]
categories: [Elasticsearch]
---

> 全文搜索引擎,分布式文档数据库,每个字段均是被索引的数据且可被搜索.

> 分片机制, 复制机制.

> Lucene是一个Java开发的全文检索引擎工具包, Elasticsearch是Lucene加了一层json.

>  Elasticsearch内置了对分布式集群和分布式索引的管理.


 
 
         
Lucene的评分机制, 查询DSL, 底层索引控制.

## Elasticsearch术语
    索引词(term): 能够被索引的精确值.
    文本(text): 普通的非结构化文字, 会被分析成一个个的索引词.
    分析(analysis): 将文本转换为索引词的过程.
    集群(cluster)
    节点(node)
    路由(routing): 存储文档时通过散列值进行选择分片.
    分片(shard): 单个Lucene实例. 索引是指向主分片和副本分片的逻辑空间,索引可以
        分解为多个分片,Elasticsearch会自动管理集群中的所有分片.
    主分片(primary shard): 默认一个索引有5个主分片.
    副本分片(replica shard): 每一个分片有零个或多个副本.
        当主分片失败时,可以从副本中选择一个作为主分片.
        并行操作提高性能和吞吐量.
    索引(index) 具有相同结构的文档集合
    类型(type) 索引中可以定义一个或多个类型,类型是索引的逻辑分区.
                一个类型被定义为具有一组公共字段的文档.
                如: 一个博客平台的所有数据存储在一个索引中,在这个索引中,定义一种
                类型为用户数据,一种类型为博客数据,一种类型为评论数据.
    文档(document) JSON格式的字符串,像关系数据库中的一行.每个文档都有一个类型和一个ID
    映射(mapping) 像关系数据库的表结构,每一个索引都有一个映射,定义了索引中的每一个字段类型,以及一个索引范围内的设置.
    字段(field) 像关系数据库中的列.每个字段对应一个类型: 整数,字符串,对象等.
    来源字段(source field) 原文档被存储在_source这个字段中.
    主键(ID) 

## API约定
    多索引参数
        同时查询多个索引中的数据, 通配符 * : test*表示查询所有以test开头的索引. -test表示排除test
    日期筛选
        <static_name{date_math_expr{date_format|time_zone}}>
        static_name: 索引名称
        date_math_expr: 动态日期计算表达式
        date_format: 日期格式
        time_zone: 时区,默认UTC
    通用参数
        pretty
        human
		
> POST改变对象的当前状态, PUT创建一个对象.		
		
> Elasticsearch从5.X引入text和keyword，keyword适用于不分词字段，搜索时只能完全匹配.
			   6.X彻底移除string, "index"的值只能是boolean变量.

