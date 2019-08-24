---
title: MyBatis
date: 2019-04-04 12:01:22
tags: [MyBatis]
categories: [MyBatis]
---

## MyBatis实操经验












## MyBatis整体架构
- 基础支持层
    - 数据源模块（连接池功能，检测连接状态）
    - 事务管理模块，缓存模块（一级、二级缓存，运行在同一个JVM）
    - Binding模块（将用户自定义的Mapper接口与映射配置文件关联起来）
    - 反射模块（封装java原生的反射，提供优化，缓存了类的元数据，提高反射性能）
    - 类型转换模块（别名机制，JDBC类型和Java类型的转换）
    - 日志模块（集成第三方日志模块）
    - 资源加载模块（对类加载器进行封装，确定类加载器的使用顺序
    - 提供了加载类文件和其他资源文件的功能）
    - 解析器模块（对XPath进行封装，为解析config文件提供支持；为处理动态SQL语句中的占位符提供支持）

- 核心处理层
    - 配置解析（加载mybatis-config.xml、*Mapper.xml和Mapper接口中的注解信息，解析后形成相应的对象保存到Configuration对象中，利用该对象创建SqlSessionFactory）
    - 参数映射，SQL解析，SQL执行（Executor维护一级、二级缓存，提供事务管理的相关操作，将数据库相关操作委托给StatementHandler完成。StatementHandler通过
          ParameterHandler完成SQL语句的实参绑定，然后通过java.sql.Statement对象执行SQL语句并得到结果集ResultSet，最后通过ResultSetHandler完成结果集映射，得到结果并返回），结果集映射，插件（拦截SQL语句进行重写）。
          
- 接口层：SqlSession

- 解析器模块, XML解析方式
    - DOM(Document Object Model)
        - 树形结构解析，将整个XML文档读入内存构建DOM树：文档节点、元素节点、属性节点、文本节点、注释节点。
        - 易于编程、节点间导航和修改数据，资源消耗大。
    - SAX(Simple API for XML)
        - 基于事件模型解析，推模式。解析到某类型节点时会触发注册在该类型节点上的回调函数。
        - 流式处理，需要自己维护节点之间的关系。
    - StAX(Streaming API for XML)
        - 拉模式，应用程序通过调用解析器推进解析进程。
            - 两套API：基于指针的，效率高但抽象程度低；基于迭代器的，应用程序把XML文档当做一系列事件对象来处理，效率低但抽象程度高。

> MyBatis在初始化过程中使用DOM解析方式。XPath是一种查询XML文档的语言。

> Java基本类型有默认值，在动态SQL部分属性判断总不为空，因此在实体类中使用包装类型。
> Java中的byte[]对应数据库中的BOLB、LONGVARBINARY等
> insert标签包含flushCache属性，默认为true，语句被调用时清空一、二级缓存。
> 给参数配置@Param注解后，MyBatis自动将参数封装为Map类型，@Param注解值作为Map中的key。

动态SQL：
 
- if：用在where语句中，通过判断参数值来决定是否使用某个查询条件；用在update语句中判断是否更新某一个字段；用在insert语句中判断是否插入某个字段的值。
- choose(when、otherwise)：实现if...else逻辑
- trim(where、set)：
    -  where: 如果该标签有返回值，就插入一个where，如果where后面的字符串是以and和or开头，则将它们剔除。
    -  set： 如果该标签有返回值，就插入一个set，如果set后面的字符串是以逗号结尾，则将逗号剔除。
    - 如果set元素中没有内容，set后面连着where，会产生SQL错误，因此需要在set标签最后加上id = #{id}
- trim: 
    - prefix: 当trim元素包含内容时，给内容增加prefix指定的前缀
    - suffix: 当trim元素包含内容时，给内容增加suffix指定的前缀
    - prefixOverrides: 当trim元素包含内容时，会把内容中匹配的前缀字符串去掉
    - suffixOverrides: 当trim元素包含内容时，会把内容中匹配的后缀字符串去掉
- foreach：${} 配合 in 的方式不能防止sql注入。可以用#{} 配合 foreach
    - collection: 要迭代循环的属性
    - item：变量名
    - index：索引属性名，List中为当前索引值，Map中为key
    - open：整个循环内容开头的字符串
    - close：整个循环内容结尾的字符串
    - separator：每次循环的分隔符
    - collection的属性设置
        - 只有一个数组参数或者集合参数
        - 批量插入
- bind： 防止sql注入。创建变量并绑定到上下文中。

${} 与 #{}
    #{}：解析为一个JDBC预编译语句的参数标记符，参数占位符。
    ${}: 仅仅为String替换，在动态SQL解析阶段进行变量替换。

高级查询
    高级结果映射
        
    存储过程

    枚举或其他对象

缓存配置
    一级缓存    
    
    二级缓存


@SpringBootTest 注解必须要加
路径前面的/不能省略
@RequestBody 在实体类上加上 @JsonRootName(value = "user")
这么写json {"user":{"email":"sanshi.30@qq.com","username":"Ramble","password":"1234567"}}