---
title: Linux性能优化
date: 2019-06-29 17:55:26
tags: [Linux]
categories: [Linux]
---

计算CPU每秒定时中断的次数:
cat /proc/interrupts | grep timer; sleep 10; cat /proc/interrupts | grep timer
    结果: 
          CPU0        CPU1
    LOC: 2142174276 2150800894
    LOC: 2142179825 2150806783
CPU0: (2142179825 - 2142174276) / 10 = 555次中断 / 秒
CPU1: (2150806783 - 2150800894) / 10 = 589次中断 / 秒

(我的腾讯云: 357次中断 / 秒)

虚拟内存统计: vmstat [-n] [-s] [delay [count]]
    参数说明: 
            delay: vmstat采样的间隔时间
            count: 采样次数
            -s: 更详细的信息
    功能: 正在运行的进程个数, CPU的使用情况, CPU接收的中断个数, 调度器执行的上下文切换次数
    模式: 采样模式和平均模式. 不指定参数则运行于平均模式,显示从系统启动以来所有统计数据的均值.
          指定延迟后, 第一个采样仍是系统启动以来的均值,之后按延迟秒数采样系统并显示.
    直接运行vmstat的结果:
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 1  0      0 198800  68680 488468    0    0     2     7    0    0  0  0 99  0  0
    结果说明
        r: 当前可运行的进程数, 已经准备好. 理想的话, 可运行进程数应与可用CPU的数量相等
        b: 等待IO完成的被阻塞进程数
        forks: 创建新进程的次数
        in: 系统发生中断的次数
        cs: 系统发生上下文切换的次数
        us: 用户进程消耗总CPU时间的百分比
        sy: 系统代码消耗的总CPU时间百分比
        wa: 等待IO消耗的CPU时间百分比
        id: 系统空闲的消耗总CPU时间百分比

    把结果输出到文件: vmstat 1 | tee /tmp/output

把占用CPU最多的进程按降序排列: top [d delay] [C] [H] [i] [n iter] [b]
        (默认每3秒更新一次)
    两种模式选项: 命令行模式, 运行时模式
    参数说明: i 不显示未使用任何cpu的进程
             H 显示应用程序所有的单个线程
             