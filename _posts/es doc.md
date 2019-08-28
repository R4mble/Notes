- 新建文档

    PUT /customer/_doc/1
    {
    "name": "John Doe"
    }

- 获取文档

    GET /customer/_doc/1

- 批量导入

    curl -H "Content-Type: application/json" -XPOST "localhost:9200/bank/_bulk?pretty&refresh" --data-binary "@accounts.json"

    curl "localhost:9200/_cat/indices?v"

- 搜索

    GET /bank/_search
    {
        "query": {
            "match_all": {}
        },
        "sort": [
            {
                "account_number": {
                    "order": "desc"
                }
            }
        ],
        "from": 10,
        "size": 10
    }
    from和size做分页

- 匹配搜索
    GET /bank/_search
    {
        "query": {
            "match": {
                "address": "mill lane"
            }
        }
    }    

- 精确搜索
    GET /bank/_search
    {
        "query": {
            "match_phrase": {
                "address": "mill lane"
            }
        }
    }

- must,should,must_not从句
    must: 文档必须完全匹配条件
    should: should下面会带一个以上的条件，至少满足一个条件，这个文档就符合should
    must_not: 文档必须不匹配条件

    GET /bank/_search
    {
        "query": {
            "bool": {
                "must": [
                    {"match": {"age": "40"}}
                ],
                "must_not": [
                    {"match": {"state": "ID"}}
                ]
            }
        }
    }

- 范围过滤器

    GET /bank/_search
    {
        "query": {
            "bool": {
                "must": {"match_all": {}},
                "filter": {
                    "range": {
                        "balance": {
                            "gte": 20000,
                            "lte": 30000
                        }
                    }
                }
            }
        }
    }