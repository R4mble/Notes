---
title: Spark实战
date: 2019-04-05 02:38:56
categories: [分布式]
---
Spark
    快速: Spark有一个Directed Acyclic Graph(DAG有向无环图)执行引擎,支持循环数据流和内存计算.

    Spark Core
        任务调度,内存管理,故障恢复,存储系统的交互
        核心思想: 将数据集缓存在内存中,用Lineage机制来进行容错

        RDD
            自动容错,位置感知调度,可伸缩性
            容错性方式:
                数据检查点(传输成本高), 记录数据的更新(只支持粗粒度的转换)

        编程接口,2类API
            Transformations (懒策略, 只有在action提交时才被触发执行)
                map, filter, union, sample, join
            Actions
                count, collect, save, reduce, lookup

        依赖关系
            窄依赖
                一个父RDD最多被一个子RDD引用,如 map, filter, union
            宽依赖
               一个父RDD被多个子RDD引用,如groupByKey

        Stage DAG
            Spark提交Job生成多个Stage, Stage之间存在依赖, 这些依赖关系构成了DAG
            对于窄依赖,spark尽量把RDD转换放在一个Stage中
            对于宽依赖,spark将Stage定义为ShuffleMapStage,以便向MapOutputTracker
                注册shuffle操作. Spark将shuffle操作定义为stage的边界.

    Spark Streaming
        对实时数据流进行高通量,容错处理的流式处理系统,可以对多种数据源(Kafka,Flume)
          进行map,reduce,join,window等操作,并将结果保存到外部文件系统,数据库等.

         原理: 将Stream数据分成小的时间片段,以类似batch批量处理的方式来处理这一小部分数据

    GraphX
        提供了一套图算法工具包,方便用户对图进行分析
            分布式图计算框架
                图存储模式
                    边分割
                        每个顶点存储一次,但有的边会被打断分到两台机器
                        好处: 节省存储空间
                        坏处: 对一条两个顶点被分到不同的机器上的边来说,要跨机器通信
                                传输数据,内网通信流量大

                    点分割
                        每条边存储一次.邻居多的点会被复制到多台机器上,增加了存储开销
                           引发数据同步问题

                图计算模型
                    BSP(Bulk Synchronous Parallel)计算模式
                        一次计算过程由一系列全局超步组成,每一个超步由并发计算,通信和
                        栅栏同步三个步骤组成. 同步完成标志着这个超步的完成以及下一个
                        超步的开始.

                     Pregel模型
                        消息通信范式. 只需要实现一个顶点更新函数, 让框架在遍历
                        顶点时进行调用.

                     GAS模型
                        共享内存.Gather,Apply,Scatter


    Spark SQL

    MLlib
        二元分类
            监督学习.如判断邮件是否是垃圾邮件. 返回一个模型,对标签未知的新个体进行
            潜在标签预测.

            两个适用于二元分类的标准模型家族
                线性支持向量机(SVMs)
                逻辑回归

                输入一个正则项参数和多个梯度下降相关参数


        线性回归
            监督学习.

        聚类
            非监督学习.KMeans
        协同过滤
            用于推荐系统. 交替最小二乘法.

        梯度下降优化基础算法
        朴素贝叶斯算法
        决策树的解析

    Spark整体流程
        Client(提交应用) -> Master(找到Worker,启动Driver) -> Driver -> RDD Graph (由DAGScheduler转化为有向无环图) -> TaskScheduler -> Executor

    Spark算子
        Value型Transformation
            输入分区与输出分区一对一
                map
                flatMap
                mapPartitions
                glom

            输入分区与输出分区多对一
                union
                cartesian 笛卡尔积

            输入分区与输出分区多对多
                groupBy

            输出分区为输入分区子集
                filter
                distinct
                subtract
                sample
                takeSample

            一对一 Cache型
                cache
                persist


        Key-Value型Transformation

        Action型,会触发SparkContext提交Job
            无输出
                foreach
            HDFS
                saveAsTextFile
                saveAsObjectFile
            Scala
                collect
                collectAsMap
                reduceByKeyLocally  先reduce再collectAsMap
                lookup
                count
                top
                reduce
                fold
                aggregate


    Spark工作机制
        Spark主要模块
            调度与任务分配
                RDD的Action触发Job提交,生成RDD DAG,由DAGScheduler转化为
                  Stage DAG,每个Stage中产生相应的Task集合,TaskScheduler将
                  任务分发到Executor执行.Executor创建线程池,并发执行
                  每个任务对应相应的一个数据块,使用
                  用户定义的函数处理数据块.

                  对RDD的块管理通过BlockManager完成.

                层次
                    应用

                    Job

                    Stage

                    Task
                算法
                    FIFO
                    FAIR

            I/O模块
                将数据以块为单位管理

            通信控制模块
                AKKA框架

            容错模块
                Lineage Checkpoint
            Shuffle模块

# Spark SQL


# Spark Streaming