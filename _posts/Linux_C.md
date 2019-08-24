---
title: Linux进程
date: 2019-06-29 17:55:26
tags: [Linux]
categories: [Linux]
---

GCC
    gcc hello.c -o hello
    预处理
        预处理器cpp, .c -> .i
        解释预处理指令: 文件包含,宏定义,条件编译.
        删除注释
        查看一个c程序被预处理之后的样子:
            gcc hello.c -o hello.i -E
    编译
        词法分析,语法分析, 生成硬件平台的汇编语言.
        查看c程序被预处理和编译之后的汇编程序:
            gcc hello.i -o hello.s S
    汇编
        将汇编程序翻译成可重定位文件.文件已经包含处理器可以
        执行的指令流,但全局符号尚未定位.
    链接
        重定位
        合并相同权限的段

Makefile
    make: 自动检查文件的更新情况,自动进行编译的软件.
    Makefile: make的配置文件, 指定编译选项,编译环境等.
            每一个子目录有一个Makefile,有一个顶层Makefile管理
            所有的子目录Makefile.

Linux C语言
    条件编译语句:
        #ifdef DEBUG
            printf("debugging message\n");
        #endif
    打开调试信息:
        gcc debug.c -o debug -DDEBUG
        
    数组和指针
        通用性指针void
            void *p = malloc(8); //申请了8个字节未知用途的内存
            
            void指针解引用时必须转化为某种具体的数据类型指针.
            double f = 3.14;
            *(double *)p = f;
        
        函数指针:
            double func(int a, char c);
            double (*p)(int a, char c);
            p = func; //func本身就是函数地址

            double ans1 = func(100, 'x');
            double ans2 = (*p)(100, 'x');

        const指针:
            const变量不能作为switch的条件.
            希望通过指针访问某块内存的数据但不希望修改它.

            int a,b;
            const int *p = &a;
            a = 100;    //正确
            p = &b;     //正确
            *p = 100;   //错误

    结构体:
        各种初始化方法, 访问方法详见![struct.c](/linux_c/struct.c)

    共同体(联合体):
        union ex
        共用同一块内存, 适合存储互斥量.

    枚举:

    IO:
        一切皆文件, 文件类别:
            普通文件(regular)
            目录文件(directory)
            管道文件(pipe)
            套接字文件(socket)
            链接文件(link)
            字符设备文件(character)
            块设备文件(block)

        打开文件并获得文件描述符: file.c


  #include <sys/types.h>
  #include <sys/stat.h>
  #include <fcntl.h>
  
  int open(const char pathname, int flags);
  int open(const char pathname, int flags, mode_t mode);
  
  flgs: 只读O_RDONLY, 只写, 读写, 追加... 多个flag用 | 隔开.
  mode: 权限,  如 0644, 0755
  返回值: 文件描述符
  
  可以用来打开普通文件,块设备文件,字符设备文件,链接文件,管道文件,
  只能用来创建普通文件,每一种特殊文件的创建都有其特定的其他函数.
  
  #include <unistd.h>
  
  int close(int fd);
  fd: 要关闭的文件的描述符.
  返回值: 成功0, 失败1.
  可以重复关闭已关闭或未打开的文件.

 ```c
读文件: 
#include <unistd.h>

ssize_t read(int fd, void *buf, size_t count);

fd: 文件描述符
buf: 指向存放读到的数据的缓冲区
count: 要读取的字节数

返回值: 成功 -> 读到的字节数, 失败 -> -1. 

写文件:
#include <unistd.h>
ssize_t write(int fd, const void *buf, size_t count);
fd: 文件描述符
buf: 指向要写入的数据
count: 要写入的字节数

返回值: 成功 -> 写入的字节数, 失败 -> -1. 
```