---
title: Elasticsearch
date: 2019-04-27 23:33:37
tags: [读书笔记]
---

# Elasticsearch

全文搜索引擎,分布式文档数据库,每个字段均是被索引的数据且可被搜索.

分片机制, 复制机制.


> Lucene是一个Java开发的全文检索引擎工具包, Elasticsearch是Lucene加了一层json.

Elasticsearch内置了对分布式集群和分布式索引的管理.

Lucene倒排索引
    根据属性的值来查找记录.
    表现为: 一个关键词,它的频度,位置.
         1. 取得关键词. 去掉无意义介词,大小写统一,动词时态还原,过滤标点.
         2. 建立倒排索引. 关键词所在的文章号,关键词的位置, 频度
         3. 实现. 词典文件,频率文件,位置文件; 词典文件保留指向另外两个文件的指针.
         4. 压缩算法. 关键词压缩: <前缀长度,后缀>. 数字压缩:只保存与上一个值的差值.
         5. 查询单词时,Lucene对词典二元查找
         
Lucene的评分机制, 查询DSL, 底层索引控制.

Elasticsearch术语
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
    类型(type) 类型是索引的逻辑分区.
    文档(document) JSON格式的字符串,像关系数据库中的一行.
    映射(mapping) 像关系数据库的表结构,每一个索引都有一个映射,定义了索引中的每一个字段类型,以及一个索引范围内的设置.
    字段(field) 像关系数据库中的列.每个字段对应一个类型: 整数,字符串,对象等.
    来源字段(source field) 原文档被存储在_source这个字段中.
    主键(ID) 

API约定
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
		
POST改变对象的当前状态, PUT创建一个对象.		
		
Elasticsearch从5.X引入text和keyword，keyword适用于不分词字段，搜索时只能完全匹配.
			   6.X彻底移除string, "index"的值只能是boolean变量.

创建库:
    PUT http://127.0.0.1:9200/secisland
查看库状态:    
    GET http://127.0.0.1:9200/_cat/indices?v
插入数据:
    PUT http://127.0.0.1:9200/secisland/secilog/1
    参数:
        {
            "computer": "x1 carbon",
            "message" : "for coding"
        }
修改文档:
    POST http://127.0.0.1:9200/secisland/secilog/1/_update
    参数:
        {
            "doc": {
                "computer": "x1 carbon",
                "message": "lenovo"
            }
        }
查询文档:
    GET  http://127.0.0.1:9200/secisland/secilog/1/
删除文档:
    DELETE http://127.0.0.1:9200/secisland/secilog/1/
删除库:
    DELETE http://127.0.0.1:9200/secisland/



索引
    创建索引
        PUT http://127.0.0.1:9200/secisland/
        参数:
            {
                "settings": {
                    "index": {"number_of_shards": 3, "number_of_replicas": 2}
                }
            }
        修改副本数量
            PUT http://127.0.0.1:9200/secisland/_settings/
            参数:
             {
                "number_of_replicas": 1
             }

    创建自定义字段类型:
        PUT http://127.0.0.1:9200/secisland
        参数:
            {
                "settings": {"number_of_shards": 3, "number_of_replicas": 2},
                "mappings": {
					"secilog": 
						{
							"properties": 
								{
									"logType": 
										{
										   "type": "text", 
										   "index": "not_analyzed"
										}
								}
						}
					}
			}

映射
    增加映射:
        PUT http://127.0.0.1:9200/secisland/
        参数:
        {
            "mappings": {
                "log": {
                    "properties": {
                        "message": {"type": "text"}
                    }
                }
            }
        }

        添加索引名为secisland, 文档类型为log, 其中包含字段message, 字段类型是字符串.

        PUT http://127.0.0.1:9200/secisland/_mapping/user
        {
            "properties": {
                "name": {"type": "keyword"}
            }
        }
        向已存在的索引secisland添加文档类型为user, 包含字段name, 字段类型是keyword 


	多个索引设置映射
		PUT http://127.0.0.1:9200/{index}/_mapping/{type}
		{body}
		
	获取映射
		GET http://127.0.0.1:9200/secisland/_mapping/secilog
		
	获取字段映射
		GET http://127.0.0.1:9200/secisland/_mapping/secilog/field/description
			
		只返回字段为description的内容.

索引别名: 		
	类似数据库中的视图.
	
	为索引添加别名
		PUT http://127.0.0.1:9200/_alias
		{
			"actions": {"add": {"index": "secisland", "alias": "secisland_alias"}}
		}
			
	删除别名
		PUT http://127.0.0.1:9200/_alias
		{
			"actions": {"remove": {"index": "secisland", "alias": "secisland_alias"}}
		}
		
	创建过滤索引别名
		PUT http://127.0.0.1:9200/_alias
		{
			"actions": {
				"add": {
					"index": "test1", "alias": "alias2",
					"filter": {"term": {"user": "ramble"}}
				}
			}
		}


	新建文档(不需要先创建索引)
	PUT http://127.0.0.1:9200/blog/article/2 
	{
		"title": "SB ES",
		"content": "it's hard to learn",
		"tags": ["stupid", "hard"]
	}
	
	返回结果
	{
		"_index": "blog",
		"_type": "article",
		"_id": "2",
		"_version": 1,
		"result": "created",
		"_shards": {
			"total": 2,
			"successful": 1,
			"failed": 0
		},
		"_seq_no": 0,
		"_primary_term": 1
	}
	再执行一次的话, _version, _seq_no会加一, result会变成updated
	
	不指定id, ES会自动生成唯一标识符.(改用了POST方法,因为是更改而不是创建)
	POST http://127.0.0.1:9200/blog/article
	{
		"title": "SB ES",
		"content": "it's hard to learn",
		"tags": ["stupid", "hard"]
	}
	返回结果
	{
		"_index": "blog",
		"_type": "article",
		"_id": "_qm_l2sBFUlcgb1-d5N2",
		"_version": 1,
		"result": "created",
		"_shards": {
			"total": 2,
			"successful": 1,
			"failed": 0
		},
		"_seq_no": 0,
		"_primary_term": 1
	}

	检索文档
		GET http://127.0.0.1:9200/blog/article/2 
	更新文档
		POST http://127.0.0.1:9200/blog/article/1/_update
		{
			"script": "ctx._source.content = \"new content\""
		}
		只修改了content字段和version.
		
		要更新索引中的文档, ES先获取文档,从_source属性获得数据,删除旧的
		文件,更改_source属性,然后把它作为新的文档来索引.
		信息一旦在Lucene的倒排索引中存储,就不能再被更改,ES通过一个带_update
		参数的脚本来实现它.
		
		
		如果文档中没有该字段,使用upsert来提供该字段的默认值.
		POST http://127.0.0.1:9200/blog/article/1/_update
		{
			"script": {
				"source": "ctx._source.counter += params.count",
				"lang": "painless",
				"params": {
					"count": 4
				}
		    },
			"upsert": {"counter": 0}
		}
		
		



















