---
title: Elasticsearch
date: 2019-04-27 23:33:37
tags: [读书笔记]
categories: [Elasticsearch]
---

- 创建索引


    PUT /secisland
    {
        "settings" : {
            "index": {"number_of_shards": 3, "number_of_replicas": 2}
        }
    }
    三个主分片,两个副本分片

- 修改副本分片的数量


    PUT /secisland/_settings
    {
        "number_of_replicas": 1
    }

- 创建自定义字段类型


    PUT /secisland
    {
        "settings" : {
           "number_of_shards": 3, "number_of_replicas": 2
        },
        "mappings": {
            "secilog": {
                "properties": {
                    "logType": {
                        "type": "string",
                        "index": "not_analyzed"
                    }
                }
            }
        }
    }

    PUT /secisland/_mapping/secilog
    {
        "properties": {
            "logType": {
                "type": "string"
            }
        }
    }
    创建了一个名为secilog的类型，类型中有一个字段，字段的名称是logType，
    字段的数据类型是string，而且这个字段是不进行分析的。
    // 创建失败,


- 增加映射


    PUT /secisland
    {
        "mappings": {
            "log": {
                "properties": {
                    "message": {
                        "type": "string"
                    }
                }
            }
        }
    }
    // 失败.





_source属性内是原始json文档.

type已过时.一个index中最好只有一种type

- 搜索


    GET /megacorp/employee/_search
- 轻量搜索


    GET /megacorp/_search?q=last_name:Smith
- 查询表达式


    GET /megacorp/employee/_search
    {
        "query" : {
            "match" : {
                "last_name" : "Smith"
            }
        }
    }

- range过滤器


    GET /megacorp/employee/_search
    {
        "query" : {
            "bool": {
                "must": {
                    "match" : {
                        "last_name" : "smith" 
                    }
                },
                "filter": {
                    "range" : {
                        "age" : { "lt" : 30 } 
                    }
                }
            }
        }
    }

- 全文搜索


    GET /megacorp/employee/_search
    {
        "query" : {
            "match" : {
                "about" : "rock climbing"
            }
        }
    }    

- 短语搜索, 精确匹配


    GET /megacorp/employee/_search
    {
        "query" : {
            "match_phrase" : {
                "about" : "rock climbing"
            }
        }
    }    
