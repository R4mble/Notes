---
title: TCP粘包和拆包
date: 2019-04-02 08:52:45
categories: [网络协议]
---

TCP是没有界限的一串数据，它会根据TCP缓冲区的实际情况进行包的划分。
一个完整的包会被TCP拆分为多个包进行发送，多个小包也可能会被封装为一个
大的数据包发送， 这就是TCP粘包和拆包问题。

发生的原因：
    1.应用程序write写入的字节大小大于套接口发送缓冲区大小。
    2.进行MSS大小的TCP分段
    3.以太网帧的payload大于MTU进行IP分片

底层无法保证数据包不被拆分，只能通过上层应用协议栈设计来解决。
    解决方案：
        1.消息定长，如每个报文固定大小为200字节，不够的话空位补空格
        2.在包尾增加回车换行符进行分割，如FTP协议
        3.将消息分为消息头和消息体，消息头中包含表示消息总长度的字段。
            通常为消息头的第一个字段使用int32来表示消息的总长度。

Netty解决TCP粘包问题
    LineBasedFrameDecoder:
        依次遍历ByteBuf中的可读字节，判断是否有\n或\r\n，如果有，就以此为结束标志，
        从可读索引到结束位置区间的字节就组成了一行。
        它是以换行符为结束标志的解码器，支持携带结束符或者不携带结束符两种解码方式，
        同时支持配置单行的最大长度。如果连续读到最大长度后仍然没有发现换行符，就会抛出
        异常，同时忽略之前读到的异常码流。

    StringDecoder:
        将接收到的对象转换成字符串，然后继续调用后面的Handler。

    
    LineBasedFrameDecoder + StringDecoder组合就是按行切换的文本解码器。

    指定分隔符：
    DelimiterBasedFrameDecoder + StringDecoder
    定长：
    FixedLengthFrameDecoder + StringDecoder