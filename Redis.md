# Redis
------
>  用途:  数据库, 缓存, 消息中间件, timeline, 计数器, 反向索引, 排行榜, 任务调度, 队列, 分布式锁.

### 数据类型
- #### 字符串(string)
		常规操作:
		​	 SET name "Tom"
		​	 GET name
		​	 APPEND name "Jack"
			EXISTS name
			DEL name
	   批量操作:
			MGET name1 name2 name3
			MSET name1 tom name2 jerry name3 jack
		设置过期:
			EXPIRE name 5		#5s后过期
			SETEX name 5 "tom"  #5s后过期
		计数:
		​	SET counter 1000
		​	INCR counter 
		​	DECR counter
		   INCRBY counter 5
		   
		SETNX name "jerry"		#如果name不存在,则创建.


	> 动态字符串,采用预分配冗余空间的方式来减少内存的频繁分配.字符串
		长度小于1M时,加倍扩容,大于1M时每次扩1M.最大长度为512M.


- #### 列表(list)
	​	LPUSH lists redis mongodb golang
	​	RPUSH lists mysql postgre
	​	LPOP lists
	​	RPOP lists
	​	LRANGE lists 0 10
	
	> 数据量较少时会使用一块连续的内存存储,压缩列表ziplist将元素连续存储.
	  数据量较多时使用quicklist. 
	  Redis将链表和ziplist结合组成quicklist,多个ziplist使用双向
	  指针串起来使用.

- #### 散列(hash)
	​	HMSET car name "volvo" color "black" price 20
	​	HMGET car name
	​	HEXISTS car name
	​	HDEL car price
	
	> 数组 + 链表的二维结构, 第一维数组位置碰撞后,使用链表将元素串接起来.
		与Java的HashMap的不同点: 
			Redis字典只能是字符串;
			Redis采用渐进式rehash策略,HashMap采用一次性全部rehash.
				Redis不堵塞服务,实现了高性能.

		渐进式rehash: 在rehash的同时保留新旧两个hash结构,查询时会同时查询两个
		hash结构,然后在

		HashMap的rehash: 

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


































