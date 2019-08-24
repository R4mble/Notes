---
title: Linux进程
date: 2019-06-29 17:55:26
tags: [Linux]
categories: [Linux]
---

阻塞blocking IO
非阻塞nonblocking IO
io多路复用技术 IO multiplexing
        select 和 poll
事件驱动 epoll

epoll 多路复用IO接口select/poll的增强版本
        显著提高程序在大量并发连接中只有少量活跃的情况下的系统CPU利用率.
        原因: 1.复用文件描述符集合来传递结果,而不用迫使开发者每次等待事件之前都必须重新准备要被侦听的文件描述符集合.
              2.获取事件时,无须遍历整个被侦听的描述符集合,只要遍历那些被内核IO事件异步唤醒而加入Ready队列的描述符集合.
              3.epoll除了提供select/poll那种IO事件的电平触发(Level Triggered),还提供了边沿触发(Edge Triggered),使得用户空间
                程序有可能缓存IO状态,减少epoll_wait/epoll_pwait的调用,提高应用程序效率.
              4.epoll没有文件描述符的上限,select/poll有限定数量.
              5.select/poll会轮询所有fd,有数据就处理,没数据就跳过.epoll只处理就绪的fd,它有一个就绪设备的队列,
                每次只轮询该队列的数据,然后进行处理.
              6.epoll使用mmap(文件/对象的内存映射,被映射到多个内存页上),同一块内存避免fd在操作过程中的拷贝问题.
              
        epoll的两种工作方式
                LT(电平触发 Level Triggered, 只要有数据可以读,不管怎样都会通知): 默认工作方式,同时支持block和no_block_socket. 
                        这种工作方式下,内核会通知你一个fd是否就绪,然后才可以对这个就绪的fd进行IO操作. 
                        即使没有任何操作,系统还是会继续提示fd已就绪,select/poll也是使用这种工作方式.

                ET(边沿触发 Edge Triggered, 只有状态发生变化时才会通知): 高速工作方式,仅支持no_block_socket,当fd从未就绪变为就绪时,
                        内核会通知fd已就绪,并且不会再次通知,除非因为某些操作导致fd就绪状态发生变化. only once.
                        当一直不对这个fd进行IO操作,导致fd变为未就绪时,内核同样不会发送更多的通知. 这种方式出错率较高,需要检测程序.

        函数
                int epoll_create(int size);
                        创建一个epoll实例,通知内核监听size个fd. 创建成功后会占用一个fd,使用完之后要close(),否则fd可能会被耗尽
                int epoll_ctl(int epfd, int op, int fd, struct epoll_events* event);
                        注册每一个文件描述符,一旦某个文件描述符就绪,内核采用回调机制,迅速激活文件描述符
                int epoll_wait(int epfd, struct epoll_event* events, int maxevents, int timeout);
                        返回一个代表就绪描述符数量的值. mmap内存映射技术,省掉文件描述符在系统调用时复制的开销

        Nginx使用了边缘触发:
                ee.events = EPOLLIN|EPOLLOUT|EPOLLET

select  
        文件描述符集合1 文件描述符集合2 文件描述符集合3                
        可读事件        可写事件        异常事件

        创建三个关注事件的文件描述符,死循环,依次遍历所有的文件描述符集合,判断是否由可读,可写,异常事件,
        遍历完,如果有事件或超时,返回事件个数,否则阻塞一定时间.
        每次调用select,都需要把三个fd集合从用户态拷贝到内核态.

        文件描述符个数有限制: 1024, 数据结构: 位图.

poll
        共用一个文件描述符集合,数据结构:链表, 
        没有最大文件描述符数量的限制

        select和poll一般不会丢失就绪的消息,使用水平触发. 将就绪的文件告诉进程后,如果进程没有对其进行IO操作,下次调用select()或poll()时
        将再次报告这些文件描述符.

epoll
        epoll_ctl: 1.增加监听的fd到红黑树中
                   2.从红黑树中删除fd
                   3.修改已经注册的fd的监听事件

        epoll_wait:如果就绪队列不为空或超时,结束循环,否则阻塞一段时间继续下一次循环,
                  如果就绪队列不为空,拷贝就绪文件描述符到用户空间,返回就绪个数.

        当设备就绪后,会通过注册的回调函数自动将就绪文件描述符加入就绪队列.

            数据结构:红黑树










