---
title: linux
date: 2019-05-30 13:07:21
tags:
---
# uptime
平均负载是指单位时间内,系统处于可运行状态和不可中断状态的平均进程数.即平均活跃进程数.

内核架构
    Linux内核上下层通信方式

常用命令:
awk
    每行按空格或TAB分割，输出文本中的1、4项
    awk '{print $1,$4}' log.txt

    格式化输出
    awk '{printf "%-8s %-10s\n",$1,$4}' log.txt

    使用","分割
    awk -F, '{print $1,$2}' log.txt

    使用内建变量
    awk 'BEGIN{FS=","} {print $1,$2}' log.txt

    使用多个分隔符, 先使用空格分割，然后对分割结果再使用","分割
    awk -F '[ ,]' '{print $1,$2,$5}' log.txt

    设置变量a=1, b=s (字符串加数字结果为数字, $1b相当于concat)
    awk -va=1 '{print $1,$1+a}' log.txt
    awk -va=1 -vb=s '{print $1,$1+a,$1b}' log.txt

    过滤第一列大于2的行
    awk '$1>2' log.txt

    过滤第一列等于2的行
    awk '$1==2 {print $1,$3}' log.txt

    过滤第一列大于2并且第二列等于'Are'的行
    awk '$1>2 && $2=="Are" {print $1,$2,$3}' log.txt

    内建变量: 
        FILENAME: 当前文件名
        NR: 行号, 从1开始

    指定输出分割符
    awk '{print $1,$2,$5}' OFS=" $ "  log.txt

    ~ 表示模式开始。// 中是模式。
    找到第二列包含 "th"的行，打印第二列与第四列
    awk '$2 ~ /th/ {print $2,$4}' log.txt

    输出包含"re" 的行
    awk '/re/ ' log.txt

    忽略大小写
    awk 'BEGIN{IGNORECASE=1} /this/' log.txt

    计算文件大小
    ls -l *.txt | awk '{sum+=$6} END {print sum}'

    从文件中找出长度大于80的行
    awk 'length>80' log.txt


sed
    在第四行后添加一行，并将结果输出到标准输出
    sed -e 4a\newline testfile

    在第二行前添加一行，并将结果输出到标准输出
    sed -e '2i drink tea' testfile

    删除第二行和第三行
    sed -e '2,3d' testfile

    删除第二行到最后一行
    sed -e '2,$d' testfile

    添加多行,使用\隔开

    第二行到第五行整个使用No 2-5 number替换
    sed '2,5c No 2-5 number' testfile

    列出第2-4行
    sed -n '2,4p' testfile

    搜索匹配Linux的行
    sed -n '/Linux/p' testfile

    删除匹配的行
    sed '/Linux/d' testfile

    替换匹配的行
    sed 's/Linux/linux/g' testfile

    行尾的.换成! 并写入文件
    sed -i 's/\.$/\!/g' testfile
    

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