---
title: PostgreSQL
date: 2019-04-04 12:01:22
categories: [SQL]
---

## 特点
- multi-version concurrency control (MVCC), Oracle中的快照隔离
- 面向对象, 可以添加自定义函数.
- 可拓展, 可以自定义数据类型,索引类型. 

## PostgreSQL与MySQL的对比:
  1. MySQL多表连接查询只支持Nest Loop,不支持hash join和sort merge join,子查询性能较低,不支持sequence
  2. 性能优化工具和度量信息不足, 异步复制,无法通过主从做到数据零丢失
  3. 在线操作功能弱.

## 数据类型
- 基本数据类型: Integer, Numeric, String, Boolean, 二进制bytea, 
- 字符型: varchar(n), char(n), text
- 位串类型: bit(n), bit varying(n)
- 枚举类型: enum
- 网络地址类型: cidr, inet, macaddr
- 结构型: Data/Time, Array, Range, UUID
- 文档型: JSON, XML, K-V
- 几何型: 点, 线, 圈, 多边形
- 定制型: 组合, 定制类型

## 数据完整性
- UNIQUE, NOT NULL
- 主键
- 外键
- 排除约束
- Explicit Locks
- Advisory Locks

## 数组
- 创建   
>    create table test_array (
        id          integer,
        array_i     integer[],
        array_t     text[]
    )
    
- 插入
    - 花括号表示
    >    insert into test_array(id, array_i, array_t) 
         values (1, '{1,2,3}', '{"a", "b", "c"}');
    - array关键字
    >   insert into test_array(id, array_i, array_t) 
        values (1, array[1,2,3], array['a', 'b', 'c']);
- 查询
    - 查询数组所有元素
    > select array_i from test_array where id=1;
    - 查询数组指定索引. 索引为1到n, n是数组长度
    > select array_i[1], array_t[3] from test_array where id=1;    
- 追加
    > select array_append(array[1,2,3], 4);  
      select array[1,2,3] || 4;
- 删除  
    移除数组中所有等于给定值的元素
    > select array_remove(array[1,2,3,2], 2);  
- 修改      
    > update test_array set array_i[3]=4 where id=1;  
    更新整个数组:   
    update test_array set array_i=array[5,6,7] where id=1;
- 替换
    > array_replace(arr, srcEle, tarEle) 替换数组元素   
    select array_replace(array[1,2,5,4], 5, 10);  
    => {1,2,10,4}

- 元素第一次出现的位置
    > select array_position(array['a','b','c','d'], 'd');
    => 4   

- to_string   
    > array_to_string(arr, separator, replaceNull)   
    select array_to_string(array[1,2,null,3], ', ', '10');  
    => 1,2,10,3
    
- 其他函数
    > @> 包含  
      <@ 被包含   
      && 重叠  
      || 串接  
      array_length 数组长度  


## json/jsonb
### 区别
1.  json数据类型存储格式为文本,而jsonb数据类型存储格式为二进制  
    检索json数据时必须重新解析,而jsonb类型不需要.  
    因此, json写入比jsonb快,检索比jsonb慢
2. jsonb输出键的顺序和输入不一样, json输出键的顺序和输入完全一样
3. jsonb类型会去掉输入数据键值的空格, json不会删掉空格.
4. jsonb会删掉重复的键,仅保留最后一个. json会保留重复的键值.
> 推荐: 大多数场景下使用jsonb, 对json的键顺序有要求时可以使用json
- 创建
    > create table test_json(id serial primary key, name json);
- 插入
    > insert into test_json(name) values ('{"col1":1, "col2":"francs", "col3": "male"}');
- 查询
    > select name -> 'col2' from test_json where id=1;  
      => "francs"  (json)
      以文本格式返回json字段键值  
      select name ->> 'col2' from test_json where id=1;  
      => "francs" (text)
      
- 字符串是否为顶层键值
    > select '{"a":1, "b":2}'::jsonb ? 'a';      
    => true
- 删除json数据的键/值    
    > select '{"a":1, "b":2}'::jsonb - 'a';
    => "{"b": 2}" (jsonb)  
    - 删除json数组中的值,索引从0开始.  
    >  select '["red", "green", "blue"]'::jsonb -0;
    => ["green", "blue"]
    - 删除嵌套json使用#-加上数组形式的删除路径
    > select '{"name":  "James",  "contact":  {"phone":  "01234  567890",  "fax":
        "01987543210"}}'::jsonb #- '{contact, fax}'::text[]  
    => "{"name": "James", "contact": {"phone": "01234  567890"}}"
    - 删除嵌套的json数组: 使用text数组指明要删除的位置  
    > select '{"name": "James", "aliases": ["Jamie", "The Jamester", "J Man"]}' 
    :: jsonb #- '{alias,1}'::text[];
    => "{"name": "James", "aliases": ["Jamie", "J Man"]}"
    
    
- 扩展最外层json对象成一组键值结果集
    > select * from json_each('{"a":"foo", "b":"bar"}');  
    =>key text  value json  
        a	"foo"  
        b	"bar"  
        以文本形式返回:  
        select * from json_each_text('{"a":"foo", "b":"bar"}');  
    =>key text  value text  
        a	foo  
        b	bar  
- 把行作为json对象返回
    > select row_to_json(test_copy) from test_copy where id=1;       
- 返回最外层的json对象中的键的集合
    > select * from json_object_keys('{"a":"foo", "b":"bar"}');  
     a  
     b  
     
- jsonb追加
    > select '{"name":"francs", "age":"31"}'::jsonb || '{"gender":"male"}'::jsonb;    
    => "{"age": "31", "name": "francs", "gender": "male"}" 
    
- 更新
    - 覆盖更新
    > select '{"name":"francs", "age":"31"}' :: jsonb || '{"age": "32", "name": "francs"}';    
    - jsonb_set, true:键不存在则添加, false:键不存在则不添加
    > select jsonb_set('{"name":"francs", "age":"31"}' :: jsonb, '{age}', '"32"'::jsonb, true);  
        注意32前后有单引号  
    => "{"age": "32", "name": "francs"}"  
    > select jsonb_set('{"name":"francs", "age":"31"}'::jsonb, '{sex}', '"male"'::jsonb, true);
    =>  {"age": "31", "sex": "male", "name": "francs"}

## 类型转换
- varchar转为text
    > select cast(varchar '123' as text);
- varchar转为int4    
    > select cast(varchar '123' as int4);
- 通过::操作符转换
    > select 1::int4, 3/2::numeric;   
    
## with查询    
    with t as (
        select generate_series(1,3)
    )
    select * from t;
    -----------
    1
    2
    3 
- 递归查询


    with recursive t (x) as (
        select 1
        union
        select x + 1
        from t
        where x < 5
    )    
    select sum(x) from t;
    
层级查询实例:
    
    CREATE TABLE test_area(id int4, name varchar(32), fatherid int4);
    INSERT INTO test_area VALUES (1, '中国'   ,0);
    INSERT INTO test_area VALUES (2, '辽宁'   ,1);
    INSERT INTO test_area VALUES (3, '山东'   ,1);
    INSERT INTO test_area VALUES (4, '沈阳'   ,2);
    INSERT INTO test_area VALUES (5, '大连'   ,2);
    INSERT INTO test_area VALUES (6, '济南'   ,3);
    INSERT INTO test_area VALUES (7, '和平区' ,4);
    INSERT INTO test_area VALUES (8, '沈河区' ,4);    
    
    -- 找到和平区的所有父节点
    with recursive r as (
        select * from test_area where id = 7
        union all
        select test_area.* from test_area, r where test_area.id = r.fatherid
    )
    select * from r order by id;
    
    -- 拼接name字段, 输出"中国辽宁沈阳和平区"
     with recursive r as (
            select * from test_area where id = 7
            union all
            select test_area.* from test_area, r where test_area.id = r.fatherid
        )
        select string_agg(name, '') from (select name from r order by id) as n;
        
     -- 查找沈阳市及管辖的区
     with recursive r as (
     	select * from test_area where id = 4
     	union all
     	select test_area.* from test_area, r where test_area.fatherid = r.id
     )
     select * from r order by id;
    
     
#### 优点
- 简化SQL代码,减少SQL嵌套次数,提高可读性
- 计算一次,在主查询中多次使用
- 不需要共享查询结果时,比视图更轻量.    

## 批量插入
- 通过表数据或函数批量插入
    > insert into tbl(id, name) select id, name from user_ini;
    > insert into tbl(id, info) select generate_series(1,5), 'batch2';
- insert into values(),(),()
    > insert into tble(id, name) values (1,'a'), (2,'b');
    减少和数据库的交互,减少数据库wal日志生成,提升插入效率.
- copy或\copy元命令  
    >导出  
     copy pguser.tbl to '/home/pg10/tbl.txt'  
    导入  
      copy pguser.tbl from '/home/pg10/tbl.txt'  
      
## returning
INSERT语句后接RETURNING属性返回插入的数据；  
UPDATE语句后接RETURNING属性返回更新后的新值；  
DELETE语句后接RETURNING属性返回删除的数据      
> insert into test_re(flag) values('a') returning id;

## upsert
insert ... on conflict update

## 常用操作
- select 连接两列合并为一列
> select first_name  || ' ' ||  last_name as full_name, email from customer;
- order by时升序NULL在最后，降序NULL在最前
- distinct bcolor, fcolor 这样使用distinct时会把这两列作为整体进行去重
- > select distinct on (bcolor) bcolor, fcolor from t1 order by bcolor ,fcolor;     
    
    先按照bcolor, fcolor对行进行排序，每一组重复的bcolor中选取第一个所在的行。
- >limit n offset m 

    跳过m个，选取n个. 注意m数量不要太大  
    
   fetch是postgresql对sql标准的实现，和limit offset功能相同
  > OFFSET start { ROW | ROWS } FETCH { FIRST | NEXT } [ row_count ] { ROW | ROWS } ONLY        
 - where in 既可以接列表，也可以接一个子查询。
 - like匹配符： % 匹配任意个字符， _ 匹配单个字符
 - 和null做 = 比较，永远为false， 因此使用 is null 和 is not null

- 找到表a中有的b中没有的行: 
    > basket_a a LEFT JOIN basket_b b ON a.fruit = b.fruit WHERE b.id IS NULL;  
- 找到表a和表b中各自独特的行：
    > basket_a a FULL JOIN basket_b b ON a.fruit = b.fruit WHERE a.id IS NULL OR b.id IS NULL;
- join 等价于 inner join
- self-join 查询有层级体系的数据，同一张表中比较行。
- json与jsonb：
  - json是对输入文本的完整拷贝，保留输入的空格，重复键和顺序。处理函数必须在每个执行上重新解析。存储快，读取慢。
  - jsonb是解析输入后保存的二进制，在解析时会删除不必要的空格和重复键，顺序也会不同，使用时不用再次解析。支持索引。存储慢，读取快。


