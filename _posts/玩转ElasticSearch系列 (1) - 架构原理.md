---
title: 玩转Elasticsearch系列 (1) - 架构原理
date: 2019-09-21 10:42:45
categories: [Elasticsearch]
---
## Lucene倒排索引
根据属性的值来查找记录.
表现为: 一个关键词,它的频度,位置.
 1. 取得关键词. 去掉无意义介词,大小写统一,动词时态还原,过滤标点.
 2. 建立倒排索引. 关键词所在的文章号,关键词的位置, 频度
 3. 实现. 词典文件,频率文件,位置文件; 词典文件保留指向另外两个文件的指针.
 4. 压缩算法. 关键词压缩: <前缀长度,后缀>. 数字压缩:只保存与上一个值的差值.
 5. 查询单词时,Lucene对词典二元查找
 
 对应关系: "文章号" 对应 "文章中所有关键词"  
 倒排索引: "关键词" 对应 "拥有该关键词的所有文章"  
 关键词在文章中出现的位置和次数:  记录该词是文章中第几个关键词,(节约索引空间,词组查询快)  
 关键词按照字符顺序排列,可以使用二元搜索算法快速定位关键词.  
 
 | 关键词 | 文章号[出现频率] | 出现位置 |
 | :----: | :----: | :----: |
 | he | 2[1] | 1
 | live | 1[2] 2[1] | 2,5 2|
 以上三列分别作为词典文件,频率文件,位置文件保存.
 词典文件不仅保存了关键词,还保存了指向频率文件和位置文件的指针.
 
 假设查询单词"live", Lucene先对词典二元查找,找到该词,通过指向频率文件的指针读出
 所有文章号.返回结果.


## 准实时索引的实现
新收到的数据写到新的索引文件里.
Lucene把每次生成的倒排索引, 叫做一个段. 使用一个commit文件记录索引内所有的segment. 
生成segment的数据来源就是内存中的buffer.

1) 当前索引有3个segment可用.
2) 新接收的数据进入内存buffer
3) 内存buffer生成一个新的segment, 刷到文件系统缓存中(默认1秒间隔, Lucene即可检索到这个新的segment), 
   再由文件系统缓存刷到磁盘, commit文件同步更新

    curl -XPOST http://127.0.0.1:9200/logstash-2015.06.21/_settings -d'
    { “refresh_interval”: “10s” }


### translog提供的磁盘同步控制
ES把数据写入内存buffer的同时,另外记录了一个translog日志.
在文件系统缓存刷到磁盘的过程中如果发生了异常, ES会从commit位置开始, 恢复整个translog文件中的记录, 保证数据一致性.
真正把segment刷到磁盘, 且commit文件进行更新时, translog文件才清空.称为flush
ES默认每30分钟或当translog文件大于512MB时 主动进行一次flush
#### translog的一致性
ES默认每5秒强制刷新translog日志到磁盘上.(如果数据没备份,发生故障时确实有可能丢失5秒数据)

## segment merge的影响
独立线程来进行segment数据归并, 把零散的segment归并为少量的,每个都比较大的segment文件.
归并后较大的segment刷到磁盘后,commit文件做出相应变更, 删除之前几个小segment,改成大segment.
等检索请求都从小segment转到大segment后,删除没用的segment.

#### 归并线程的配置
归并过程: 读取segment -> 归并计算 -> 写segment -> 刷到磁盘
限速机制: indices.store.throttle.max_bytes_per_sec : 20MB
对于SSD可以调到100MB或更高:

    curl -XPOST http://127.0.0.1:9200/_cluster/settings -d'
    { “persistent”: {
            "indices.store.throttle.max_bytes_per_sec" : "100mb"
        } 
    }

归并线程的数量: Math.min(3, Runtime.getRuntime().availableProcessors() / 2)    
服务器CPU核数的一半大于3时启动3个归并线程, 否则启动CPU核心数一半的线程.
如果磁盘性能跟不上,可以降低index.merge.scheduler.max_thread_count配置, 避免IO情况恶化
归并策略:
- index.merge.policy.floor_segment 默认2MB, 小于它的优先被归并
- index.merge.policy.max_merge_at_once 默认一次最多归并10个segment
- index.merge.policy.max_merge_at_once_explicit 默认optimize时一次最多归并30个segment
- index.merge.policy.max_merged_segment 默认5GB,大于它的不参与归并,optimize除外
增大flush间隔,让每次新生成的segment本身就比较大

optimize接口: 在负载较低的时间段,强制归并segment

## routing和replica的读写过程
### routing
shard = hash(routing) % number_of_primary_shards

每个数据都有一个routing参数,默认使用_id值.
因此: Elasticsearch索引的主分片数不能随意修改, 一旦改动存储位置计算结果就会发生改变, 索引数据将不可读.
### 副本一致性


## shard的allocate控制

## 自动发现的配置