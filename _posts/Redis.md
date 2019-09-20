---
title: Redis  
date: 2019-08-25 09:21:14  
categories: [Redis]
---
>  用途:  数据库, 缓存, 消息中间件, timeline, 计数器, 反向索引, 排行榜, 任务调度, 队列, 分布式锁.

## 数据类型
### 字符串(string)
常规操作:

    set name "Tom"
    get name
    append name "Jack"
    exists name
    del name

批量操作:

    MGET name1 name2 name3
    MSET name1 tom name2 jerry name3 jack
设置过期:

    EXPIRE name 5		#5s后过期
    SETEX name 5 "tom"  #5s后过期
计数:

    SET counter 1000
    INCR counter 
    DECR counter
    INCRBY counter 5
   
    SETNX name "jerry"		#如果name不存在,则创建.


> 动态字符串,采用预分配冗余空间的方式来减少内存的频繁分配.字符串
    长度小于1M时,加倍扩容,大于1M时每次扩1M.最大长度为512M.


### 列表(list)

	LPUSH lists redis mongodb golang
	RPUSH lists mysql postgre
	LPOP lists
	RPOP lists
    LRANGE lists 0 10
	
> lindex对链表进行遍历, 性能随index变大而变差. index为-1表示倒数第一个元素.
  ltrim 保留start_index和end_index之间的区间, 其他的去掉.


> 数据量较少时会使用一块连续的内存存储,压缩列表ziplist将元素连续存储.
  数据量较多时使用quicklist. 
  Redis将链表和ziplist结合组成quicklist,多个ziplist使用双向
  指针串起来使用.这样既可以快速插入删除,也不会出现太大的空间冗余.

### 散列(hash)

	HMSET car name "volvo" color "black" price 20
	HMGET car name
	HEXISTS car name
	HDEL car price
	
> 数组 + 链表的二维结构, 第一维数组位置碰撞后,使用链表将元素串接起来.
    与Java的HashMap的不同点: 
        Redis字典只能是字符串;
        Redis采用渐进式rehash策略,HashMap采用一次性全部rehash.
            Redis不堵塞服务,实现了高性能.

    渐进式rehash: 在rehash的同时保留新旧两个hash结构,查询时会同时查询两个
    hash结构,然后在后续的定时任务中以及hash的子指令中,逐渐将旧hash内容迁移到
    新hash结构中.当hash移除了最后一个元素后,该数据结构自动被删除,内存被回收.

    HashMap的rehash: 

### 集合(set)

    SADD letters a
    SADD letters a b c
    SREM letters c d
    SMEMBERS letters
    SADD setA 1 2 3
    SDIFF setA setB
	
相当于value为NULL的字典.

### 有序集合(zset)

    ZADD tutorials 1 redis 2 mongodb 3 mysql
    ZRANGE tutorials 0 10 WITHSOCRES
    ZRANGE bookstore 0 -1       默认按score从低到高排
    ZREVRANGE bookstore 0 -1    按score从高到低排
    ZCARD bookstore 			   集合中元素的数量,key不存在时返回0
    zscore bookstore "Java Concurrency" 查看score(使用double存储,有精度问题)
    zrank bookstore "Java Concurrency" 排名,按照score从低往高排.
    zrangebysocre bookstore 0 8.9 根据score区间遍历zset
    zrem bookstore "Java Concurrency"



相当于SortedSet和HashMap的结合.
一方面作为Set保证value的唯一性,另一方面给value赋予score,使用
跳跃列表的数据结构来排序.

跳跃列表
    层级结构,最下面一层所有元素串起来,每隔几个元素挑出一个代表,将这几个
    代表使用另外一级指针串起来,然后在这些代表中再挑出二级代表,再串起来.

容器型数据结构通用规则: 
    create if not exists
    drop if no elements
如果字符串已经设置了过期时间, 然后调用set方法修改它, 它的过期时间会消失.

### 分布式锁
	setnx(set if not exist)
	分布式锁逻辑流程:
	
    setnx lock:codehole true
        do something...
    expire lock:codehole 5		
    del lock:codehole
存在问题: setnx和expire操作之间如果进程挂掉就会造成死锁.

解决办法: redis2.8后加入set指令扩展参数,使这两个指令一起执行.

    set lock:codehole true ex 5 nx
        do something...
    del lock:codehole
    
依然存在问题: 如果加锁和释放锁之间逻辑执行时间太长超出锁超时限制,
    锁会过期,第二个线程重新持有锁. 第一个线程执行完逻辑后把锁释放了,
    第三个线程就会在第二个线程逻辑执行完之前拿到了锁.
解决方案: 为set指令的value设置一个随机数,释放锁时检查随机数是否一致,
    然后再删除key. 检查value和删除key不是原子操作,需要Lua脚本来处理.

延时队列
	blpop/brpop 阻塞读, 队列没数据时立即进入休眠状态,一旦数据到来,立刻
		醒过来,消息延迟几乎为零.
	空闲连接自动断开
		闲置过久的客户端连接会被服务器主动断开,减少闲置资源占用.这时
		blpop/brpop会抛出异常.
	分布式锁加锁失败处理:
		1. 直接抛异常, 通知用户稍后再试.
		2. sleep一会儿再充实.
		3. 将请求转移至延时队列, 过一会再试.
	延时队列可以用zset来实现,消息序列化为字符串作为zset的value,这个
	消息的到期时间作为score,然后用多个线程轮询zset获取到期的任务进行
	处理.


### 位图(bitmap)
比特位组成的数组.
用途举例: 存用户一年的签到记录.

    bitcount
        统计指定位置范围内1的个数,start和end是字节索引,必须是8的倍数.
        要查找用户某个月签到多少天,需要将这个月覆盖的字节全部取出然后统计.
    bitpos 来查找指定范围内出现的第一个0或1
    bitfield 一次操作多个位

###  HyperLogLog
提供不精确的去重计数方案, 标准误差0.81%

    pfadd uv user1
    pfcount uv
    pfadd uv user2 user3 user1 user4
    pfcount uv  

pfmerge 将多个pf计数值累加形成新的pf值

布隆过滤器:
	场景举例: 推荐内容去重.
	
    bf.add 		bf.madd
    bf.exists 		bf.mexists

向布隆过滤器添加key时,使用多个hash函数对key进行hash算得一个整数索引,


### 地理空间(geospatial).

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
#### RDB
默认的持久化方式: 将服务器某个时间点上的数据库状态(非空数据库以及相关键值)保存到一个经过压缩的二进制文件中(dump.rdb)
手动持久化命令:
1. SAVE: 阻塞Redis服务器进程
2. BGSAVE: 派生出一个子进程,由子进程负责创建RDB文件,服务器父进程继续处理命令请求.
#### AOF


### Redis哨兵(Sentinel)

### 自动分区(Cluster)


### scan

    scan cursor [MATCH pattern] [COUNT count]

Redis中的key存在一个很大的字典中,数组加链表. scan指令返回的游标就是数组的位置索引,

1. 复杂度为O(n), 通过游标分步进行, 不会阻塞线程.
2. 提供limit参数, 可以控制每次返回结果的最大条数. 等于服务器单次遍历的字典槽位数量.
3. 模式匹配功能
4. 服务器不保存游标状态, 返回给客户端
5. 返回的结果可能有重复, 客户端要去重.
6. 遍历过程中如果有数据修改, 不确定能否遍历到改动后的数据.
7. 单次返回的结果为空不能说明遍历结束.要看游标值是否为零.


遍历顺序: 高位进位加法
	从左边加, 进位往右边移动.
	考虑到字典的扩容和缩容时避免槽位的遍历重复和遗漏.

字典扩容
	当loadFactor达到阈值时,需要重新分配一个新的2倍大小的数组,然后将所有的元素
	全部rehash挂到新的数组下面. 

渐进式rehash
	扩容时, 同时保留旧数组和新数组, 然后在定时任务中以及后续对hash指令操作中渐渐地将
	旧数组中挂接的元素迁移到新数组上. 操作rehash中的字典,需要同时访问新旧两个数组结构,
	如果在旧数组下面找不到元素, 还需要去新数组下面寻找.

	scan对在rehash中的字典, 需要同时扫描新旧槽位, 将结果融合后返回给客户端.

定位影响性能的大key
	redis-cli -h 127.0.0.1 -p 7001 --bigkeys -i 0.1
	每隔100条scan指令就会休眠0.1s, ops不会剧烈抬升

线程IO模型
	, select系列的事件轮询API

非阻塞IO
    在套接字对象上提供一个选项Non_Blocking, 当这个选项打开时, 读写方法不会阻塞, 读取多少取决于
    内核为套接字分配的读缓冲区内部的数据字节数, 写多少取决于内核为套接字分配的写缓冲区的空闲空间
    字节数.
    读写方法都会通过返回值来告知程序实际读写了多少字节.

事件轮询(多路复用)  (NIO技术)
    select函数: 输入: 读写描述符列表 read_fds & write_fds,  timeout
                输出: 对应的可读写事件

    select在描述符很多时性能很差.

    改用: epoll函数

定时任务:
    将定时任务记录在最小堆中, 最快要执行的任务排在堆的最上方.
    在每个循环周期,Redis将最小堆里已到点的任务立即进行处理,处理完毕后
    将最快要执行的任务还需要的时间记录下来,这个时间就是select系统调用的timeout参数.

通信协议
RESP	Redis Serialization Protocol
Redis序列化协议, 直观的文本协议, 实现简单, 解析性能好.

5中最小单元类型, 单元结束时统一加上回车换行符 \r\n
1. 单行字符串以 + 开头
2. 多行字符串以 $ 开头, 跟上字符串长度
3. 整数值以 : 开头, 后跟整数的字符串形式
4. 错误消息以 - 开头
5. 数组以 * 开头, 后跟数组的长度

单行字符串: +hello world\r\n
多行字符串: $11\r\nhello world\r\n
整数: :1024\r\n
错误: -WRONGTYPE Operation against a key holding the wrong kind of value
数组[1,2,3]: *3\r\n:1\r\n:2\r\n:3\r\n
NULL: $-1\r\n
空串: $0\r\n\r\n

客户端向服务器发送指令: 多行字符串数组
	set auther codehole
	*3\r\n$3\r\nset\r\n$6\r\nauther\r\n$8\r\ncodehole\r\n

服务器对客户端响应:
	单行字符串OK ->  +OK
	整数响应1 -> :1

持久化
	快照: 全量备份, 内存数据的二进制序列化形式,存储紧凑
	    调用glibc的函数fork产生一个子进程,快照持久化完全交给子进程来处理,
		父进程继续处理客户端请求. 子进程刚产生时与父进程共享内存中的代码段
		和数据段.
		fork函数会在父子进程同时返回, 父进程中返回子进程的pid, 子进程返回0
		如果操作系统内存资源不足,pid会是负数,表示fork失败.

		Copy on Write: 数据段页面分离, 父进程对一个页面的数据进行修改时,会将
			被共享的页面复制一份分离出来, 然后对这个复制的页面进行修改, 这时子进程
			对应的页面没有变化, 还是进程产生时那一瞬间的数据.

	AOF日志: 连续的增量备份, 内存数据修改的指令记录文本, 会在长期运行中
	      变得很庞大,数据库重启时需要加载AOF日志进行指令重放,时间很长.
		  需要定期AOF重写.
		  Redis会在收到客户端修改指令后,先进行参数校验,如果没问题就立即将该指令文本
		  存储到AOF日志中, 先存到磁盘再执行指令.

		  AOF重写
		  	bgrewriteaof
			   开启一个子进程对内存进行遍历转换成一系列Redis的操作指令,序列化到一个新的
			   AOF日志文件中, 序列化完成后再将操作期间发生的增量AOF日志追加到这个新的AOF
			   日志文件中,追加后立即替代旧的AOF日志文件.

		    fsync(int fd)
				程序对AOF日志文件进行写操作时,实际上是将内容写到了内核为文件描述符分配的一个
				内存缓存中, 然后内核会异步将脏数据刷回到磁盘.
				fsync将指定文件的内容强制从内核缓存刷新到磁盘.
				通常在生产环境的服务器里,  每隔1s左右执行一次fsync操作.

	持久化操作主要在从节点进行.
	混合持久化:
		Redis重启时先加载rdb内容,再重放增量AOF.


管道
	客户端通过对管道中的指令列表改变读写顺序, 节省IO时间.

	redis-benchmark -t set -P n -q  管道压力测试
	n表示单个管道内并行的请求数量.

	我的i5-9400, P到150时QPS可以到100W/s

	对于管道来说: 连续的write操作没有耗时,之后第一个read操作会等待一个网络的来回开销,
	然后所有的响应消息就都已经会送到内核的读缓冲了.后续的read操作直接就可以从缓冲拿到结果.













