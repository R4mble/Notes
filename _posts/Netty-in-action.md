---
title: Netty in action
date: 2019-04-05 02:36:04
tags: [读书笔记]
categories: [Netty]
---
Netty   异步和事件驱动

    Netty核心组件:
            Bootstrap 和 ServerBootstrap
                提供了一个用于应用程序网络层配置的容器

                Bootstrap
                    连接远程主机,有1个EventLoopGroup

                ServerBootstrap 绑定本地端口
                    在服务器监听一个端口轮询客户端的Bootstrap或DatagramChannel是否
                        连接服务器.   调用Bootstrap类的connect方法,也可以先bind()
                        再connect(),之后使用的Channel包含在bind()返回的ChannelFuture中
                    一个ServerBootstrap有2个Channel集合.(EventLoopGroup)
                        1.包含一个单例ServerChannel,代表持有一个绑定了本地端口的socket
                        2.包含所有创建的Channel,处理服务器所接收到的客户端进来的连接.

            Channel
                代表了一个用于连接到实体如硬件设备,文件,网络套接字或程序组件,能够执行一个或多个不同的IO操作的开放连接.
                底层网络传输API必须提供给应用I/O操作的接口.如读/写/绑定等.
                定义了与socket丰富交互的操作集:bind,close,config,connect,
                    isActive,isOpen,isWritable,read,write.

            ChannelHandler
                支持很多协议,提供用于数据处理的容器. 应用程序的核心
                由特定事件触发
                ChannelInboundHandler
                    数据从远程主机到用户应用程序
                ChannelOutboundHandler
                    数据从用户应用程序到远程主机
                体现了关注点分离的设计原则,并简化业务逻辑的迭代开发的要求.
                        每一个方法覆盖到hook在活动周期适当的点.
                        覆盖channelRead0因为需要处理所有接收到的数据
                        覆盖exceptionCaught可以应对任何Throwable的子类型,
                        这种情况记录并关闭所有可能处于未知状态的连接.
                每一个Channel都有一个关联的ChannelPipeline,它代表了ChannelHandler实例
                的链.适配器处理的实现是将一个处理方法转发到链中的下一个处理器.
                因此提供至少一个实现exceptionCaught的ChannelHandler
                ChannelHandler给不同类型的事件调用应用程序实现或扩展ChannelHandler挂接到事件生命周期和提供自定义应用逻辑

            Callback 回调   回调事件由接口ChannelHandler的实现来处理
                // 一旦一个新的连接建立, channelActive方法就会被调用
                public class ConnectHandler extends ChannelInboundHandlerAdapter {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("Client " + ctx.channel().remoteAddress() + " connected");
                    }
                }

            ChannelFuture
                Netty所有的I/O操作都是异步的,当操作完成时可以被通知.
                一个未来执行操作结果的占位符.

            ChannelPipeline
                提供一个容器给ChannelHandler链并提供一个API用于管理沿着链入站
                和出站事件的流动.
                每个Channel都有自己的ChannelPipeline,当Channel创建时自动创建

                实现了ChannelHandler的抽象ChannelInitializer.
                ChannelInitializer子类通过ServerBootstrap注册.
                当它的方法initChannel()被调用时,这个对象将安装自定义的ChannelHandler集到
                pipeline. 当这个操作完成时,ChannelInitializer子类从ChannelPipeline
                自动删除自身.

            EventLoop
                处理Channel的I/O操作. 一个单一的EventLoop会处理多个Channel事件.
                一个EventLoopGroup可以含有多于一个的EventLoop和提供了一种迭代用于
                检索清单中的下一个.


            Channel Event 和 I/O
                Netty 非阻塞,事件驱动的网络框架
                    使用多线程处理I/O事件.
                    Netty的设计保证程序处理事件不会有同步. 不需要在Channel之间共享ChannelHandler实例

                    一个EventLoopGroup有一个或多个EventLoop,一个EventLoop作为一个线程给Channel执行工作

                    当创建一个Channel,Netty通过一个单独的EventLoop实例来注册该Channel的通道的使用寿命.
                    所有Channel的I/O始终用相同的线程来执行,所以不需要同步Netty的I/O操作.




引导服务器
    监听和接收进来的连接请求
    配置Channel来通知一个关于入站消息的EchoServerHandler实例
