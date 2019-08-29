---
title: Elasticsearch
date: 2019-04-27 23:33:37
tags: [读书笔记]
categories: [Elasticsearch]
---

- 创建库  

        PUT http://127.0.0.1:9200/secisland
- 查看库状态    

        GET http://127.0.0.1:9200/_cat/indices?v
- 插入数据

        PUT http://127.0.0.1:9200/secisland/secilog/1
        参数:
            {
                "computer": "x1 carbon",
                "message" : "for coding"
            }
- 修改文档

        POST http://127.0.0.1:9200/secisland/secilog/1/_update
        参数:
            {
                "doc": {
                    "computer": "x1 carbon",
                    "message": "lenovo"
                }
            }
- 查询文档

        GET  http://127.0.0.1:9200/secisland/secilog/1/
- 删除文档

        DELETE http://127.0.0.1:9200/secisland/secilog/1/
- 删除库

        DELETE http://127.0.0.1:9200/secisland/

## 索引
- 创建三个主分片,两个副本分片的索引

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

创建自定义字段类型

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

## 映射
增加映射
- 添加索引名为secisland, 文档类型为log, 其中包含字段message, 字段类型是字符串.

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


- 向已存在的索引secisland添加文档类型为user, 包含字段name, 字段类型是keyword 

      PUT http://127.0.0.1:9200/secisland/_mapping/user
        {
            "properties": {
                "name": {"type": "keyword"}
            }
        }

- 多个索引设置映射

		PUT http://127.0.0.1:9200/{index}/_mapping/{type}
		{body}
		
- 获取映射

		GET http://127.0.0.1:9200/secisland/_mapping/secilog
		
- 获取字段映射

		GET http://127.0.0.1:9200/secisland/_mapping/secilog/field/description
			
		只返回字段为description的内容.

## 索引别名: 		
	类似数据库中的视图.
	
- 为索引添加别名

		PUT http://127.0.0.1:9200/_alias
		{
			"actions": {"add": {"index": "secisland", "alias": "secisland_alias"}}
		}
			
- 删除别名

		PUT http://127.0.0.1:9200/_alias
		{
			"actions": {"remove": {"index": "secisland", "alias": "secisland_alias"}}
		}
		
- 创建过滤索引别名

		PUT http://127.0.0.1:9200/_alias
		{
			"actions": {
				"add": {
					"index": "test1", "alias": "alias2",
					"filter": {"term": {"user": "ramble"}}
				}
			}
		}


- 新建文档(不需要先创建索引)

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
	
- 不指定id, ES会自动生成唯一标识符.(改用了POST方法,因为是更改而不是创建)

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

- 检索文档

		GET http://127.0.0.1:9200/blog/article/2 
- 更新文档

		POST http://127.0.0.1:9200/blog/article/1/_update
		{
			"script": "ctx._source.content = \"new content\""
		}
		只修改了content字段和version.
		
要更新索引中的文档, ES先获取文档,从_source属性获得数据,删除旧的
文件,更改_source属性,然后把它作为新的文档来索引.
信息一旦在Lucene的倒排索引中存储,就不能再被更改,ES通过一个带_update
参数的脚本来实现它.
		
		
- 如果文档中没有该字段,使用upsert来提供该字段的默认值.

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
		
		



















