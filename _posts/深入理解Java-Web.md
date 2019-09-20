---
title: 深入理解Java Web
date: 2019-04-05 02:40:42
tags: [读书笔记]
categories: [Java]
---
Web请求过程
    HTTP 无状态的短连接.发起一个http请求就是建立一个socket通信的过程
        Request Header
            Accept-Charset
            Accept-Encoding
            Accept-Language
            Host
            User-Agent
            Connection: Keep-Alive
        Response Header
            Server
            Content-Type
            Content-Encoding
            Content-Language
            Content-Length
            Keep-Alive: timeout=5,max=120


        常见状态码
            200     成功
            302     临时跳转, 跳转的地址通过Location指定
            304     文件未变化
            400     客户端请求有语法错误, 不能被服务器识别
            403     服务器收到请求, 但是拒绝提供服务
            404     请求的资源不存在
            500     服务器发生不可预期的错误

        浏览器缓存
            Pragma: no-cache
            Cache-Control: no-cache

        Expires

        Last-Modified/Etag
            浏览器在请求头中增加If-Modified-Since, 如果是最新的, 返回304

    DNS域名解析过程
        1. 浏览器检查缓存有没有.
        2. 操作系统缓存. hosts文件
        3. LDNS
        4. Root Server

    CDN工作机制


java io
    Java IO类库
        传输数据的数据格式
            基于字节操作的IO接口:    InputStream OutputStream
            基于字符操作的IO接口:    Writer Reader
        传输数据的方式
            基于磁盘操作的IO接口:    File
            基于网络操作的IO接口:    Socket

        字符与字节的转化接口
            InputStreamReader: 字节到字符的转化桥梁
            OutStreamWriter:    字符到字节的编码过程

    磁盘IO工作机制
        访问文件的方式
            标准访问文件方式
                read()
                    检查内核缓存,有的话直接返回, 没有的话从磁盘读取, 缓存在内核地址空间
                write()
                    从用户地址复制到内核地址空间缓存, 什么时候写到磁盘由操作系统决定,
                    除非显示调用sync同步命令

            直接IO方式
                直接访问磁盘数据,不经过操作系统内核数据缓冲区.减少一次数据复制.
                通常是在对数据的缓存管理由应用程序实现的数据库管理系统中.

            同步访问文件方式

            异步访问文件方式

            内存映射的方式
                将操作系统内存中的某一块区域与磁盘中的文件关联起来.




java编码


java编译原理:  将java源码编译成java字节码, .java->.class, 源代码->二进制数字
    模块  
        词法分析器:  找出语法关键词 -> 规范Token流
            Scanner: 具体读取和归类不同词法
            JavacParser: 规定了哪些词是符合java语言规范
        语法分析器:  检查关键词是否符合java语言规范 -> 抽象语法树
        语义分析器:  将复杂的语法转化为简单的语法(foreach 注解) -> 注解过后的语法树
        代码生成器:  生成字节码
    语法树结构
    工作流程详解
        词法分析
        语法分析
        符号表的构建
        annotation的处理
        标注和语法检查
        数据流分析
        类型转化
        语法
    实现内部类
    实现对异常的处理
    与其他编译器的比较


class文件结构


ClassLoader工作机制


JVM体系结构和工作方式
    虚拟内存
        多个进程空间上共享物理内存, 逻辑上不能互相访问
        一个虚拟地址被映射到一段物理内存,文件或其他可以寻址的存储(Swap分区)
    内核空间和用户空间
        减少从内核空间到用户空间的数据复制方式:sendfile文件传输方式

    Java中哪些组件需要使用内存
        Java堆
            堆的大小在JVM启动时一次向操作系统申请完成.
            分配完成后堆的大小固定,内存不够时再重新申请.内存空闲时也不能将多余的空间交还.

        线程
            每个线程创建时JVM都会为它创建一个堆栈, 通常在256KB-756KB之间

        类和类加载器
            存储在永久代

        NIO
            基于通道和缓冲区来执行IO的新方式.
            java.nio.ByteBuffer.allocateDirect()方法分配内存.使用本机内存而不是Java堆上内存.
                每次分配内存会调用操作系统的os::malloc()函数.

            ByteBuffer产生的数据如果和网络或者磁盘交互都在操作系统的内核空间发生,不需要
            将数据复制到java内存中.(如果io频繁发送很小的数据,系统调用的开销会抵消掉不用复制的好处)

            直接ByteBuffer对象会自动清理本机缓冲区,这个过程只能作为java堆gc的一部分来执行

        JNI

    JVM内存结构
        PC寄存器
            一个数据结构,用于保存当前正常执行的程序的内存地址,记录哪个线程当前执行到哪条指令.
        Java栈
            每当创建一个线程时,JVM就会为这个线程创建一个对应的java栈,这个栈中又会含有多个帧栈.
            每运行一个方法创建一个帧栈.帧栈含有内部变量,操作栈,方法返回值等信息
            每当方法执行完成时,帧栈弹出帧栈的元素作为方法的返回值.
        堆
        方法区
        本地方法区
        运行时常量池

JVM内存管理


Servlet工作原理
    Servlet容器
        启动过程
            启动类: org.apache.catalina.startup.Tomcat,创建实例对象并调用start方法.
                通过该对象增加和修改Tomcat配置参数, 动态增加Context,Servlet等
            基于观察者模式: 所有容器继承Lifecycle接口,它管理容器的整个生命周期,所有
                容器的修改和状态的改变都由它去通知已经注册的观察者(Listener)

    Tomcat启动时序图
        Tomcat start
            StandardServer start init
                StandardService init
                    StandardEngine init
                       Connector
                        StandardServer start
                            StandardService start
                                StandardEngine start
                                    StandardHost init start
                                        StandardContext init(ContextConfig作为观察者被通知)
                                        StandardContext init(配置解析)
                                        StandardContext startInternal
                                            StandardService start
                                                Connector init
                                                    Http11Protocol start
                                                    Http11Protocol init

    当Context容器初始化状态设为init时, 添加到Context容器的Listener将会被调用.
    ContextConfig继承了LifecycleListener接口, 它是在调用Tomcat.addWebapp时被加入到
    StandardContext容器中的.
    ContextConfig类会负责整个Web应用的配置文件的解析工作.

    ContextConfig的init方法
        创建用于解析XML配置文件的contextDigester对象
        读取默认的context.xml配置文件,如果存在则解析它
        读取默认的Host配置文件,如果存在则解析它
        读取默认的Context自身的配置文件,如果存在则解析它
        设置Context的DocBase

    Context容器的startInternal方法
        创建读取资源文件的对象
        创建ClassLoader对象
        设置应用的工作目录
        启动相关的辅助类(如Logger,realm,resources)
        修改启动状态, 通知感兴趣的观察者(Web应用的配置)
        子容器的初始化
        获取ServletContext并设置必要的参数
        初始化"load on startup"的Servlet

    Web应用的初始化工作(在ContextConfig的configureStart方法中实现)
        解析web.xml文件, 其中的各个配置项会被解析成相应的属性保存在WebXml对象中
        将WebXml对象中的属性设置到Context容器中,包括创建Servlet对象,filter.listener等

        将Servlet包装成Context容器中的StandardWrapper并作为子容器添加到Context中

    创建Servlet实例(Wrapper.loadServlet实现)
        web.xml默认配置项(Tomcat启动时这两个Servlet就会被启动)
            DefaultServlet
            JspServlet
        loadServlet方法获取servletClass,交给InstanceManager去创建一个基于
        servletClass.class的对象
        如果这个Servlet配置了jsp-file,那这个servletClass就是JspServlet

    初始化Servlet(StandardWrapper的initServlet方法)
        initServlet
            调用Servlet的init()方法,同时把包装了StandardWrapper对象
            的StandardWrapperFacade做为ServletConfig传给Servlet.


    Servlet体系结构(握手型的交互式:两个模块为了交换数据准备一个交易场景)
        StandardWrapper和StandardWrapperFacade都实现了ServletConfig接口.
        StandardWrapperFacade传给Servlet,保证从StandardWrapper中拿到ServletConfig
        所规定的数据,又不把ServletConfig不关心的数据暴露给Servlet

        ServletContext ApplicationContext ApplicationContextFacade
            保证ServletContext只能从从容器拿到它该拿的数据.




Session与Cookie


Tomcat系统架构与设计模式
    Tomcat
        Container容器
            Engine
                Host
                    Servlet容器
                        Context(对应一个Web工程)
                            Wrapper(Servlet包装类)

Jetty工作原理解析


Spring设计理念


SpringMVC工作机制


MyBatis系统架构


Velocity工作原理
