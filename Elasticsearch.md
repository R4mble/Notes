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

映射

搜索

聚合

