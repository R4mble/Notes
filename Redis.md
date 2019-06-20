# Redis
------
>  用途:  数据库, 缓存, 消息中间件, timeline, 计数器, 反向索引, 排行榜, 任务调度, 队列, 分布式锁.

### 数据类型
- #### 字符串(string)
	​	SET name "Tom"
	​	GET name
	​	SET counter 1000
	​	INCR counter 
	​	DECR counter
	​	APPEND name "Jack"
	
- #### 散列(hash)
	​	HMSET car name "volvo" color "black" price 20
	​	HMGET car name
	​	HEXISTS car name
	​	HDEL car price
	
- #### 列表(list)
	​	LPUSH lists redis
	​	LPUSH lists mongodb
	​	RPUSH lists mysql
	​	LPOP lists
	​	RPOP lists
	​	LRANGE lists 0 10
- #### 集合(set)
	​	SADD letters a
	​	SADD letters a b c
	​	SREM letters c d
	​	SMEMBERS letters
	​	SADD setA 1 2 3
	​	SDIFF setA setB
- #### 有序集合(zset)
	​	ZADD tutorials 1 redis 2 mongodb 3 mysql
	​	ZRANGE tutorials 0 10 WITHSOCRES
- #### 位图(bitmap)
比特位组成的数组
- ####  hyperLoglog

- #### 地理空间(geospatial).

### 事务(transaction)
    MULTI
    INCR likes
    INCR visitors
    EXEC
### 排序
    LPUSH mylist 4 3 2 6 9
    SORT mylist
    LPUSH mylistalpha a d i e p
    SORT mylistalpha ALPHA

### 生存时间
    SET session uuid11
    EXPIRE session 20 
    TTL session
### 管道命令(Piplining)

### 发布/订阅
    SUBSCRIBE redisChat
    PUBLISH redisChat "Learn redis"
### 复制(replication)

### Lua脚本(Lua scripting)

### LRU驱动事件(LRU eviction)


### 磁盘持久化(persistence)
- #### RDB
##### 默认的持久化方式: 将服务器某个时间点上的数据库状态(非空数据库以及相关键值)保存到一个经过压缩的二进制文件中(dump.rdb)
##### 手动持久化命令:
1. SAVE: 阻塞Redis服务器进程
2. BGSAVE: 派生出一个子进程,由子进程负责创建RDB文件,服务器父进程继续处理命令请求.
- #### AOF


### Redis哨兵(Sentinel)

### 自动分区(Cluster)


































