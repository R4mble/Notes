---
title: linux
date: 2019-05-30 13:07:21
tags:
categories: [Linux]
---
# uptime
平均负载是指单位时间内,系统处于可运行状态和不可中断状态的平均进程数.即平均活跃进程数.

内核架构
    Linux内核上下层通信方式

常用命令:
nc -l port > test.txt
nc -q 0 MachineIP Port < test.txt

nohup npm start &

1、tar包的创建
tar -cvf  file.tar file1  file2
tar -zcvf  file.tar.gz  file1  file2
tar -jcvf   file.tar.bz2  file1  file2

2、tar包的查看
tar -tvf  file.tar
tar -ztvf  file.tar.gz
tar -jtvf  file.tar.bz2

3、释放tar包
tar -xvf  file.tar
tar -zxvf  file.tar.gz
tar -jxvf  file.tar.bz2

主选项：
-x    从档案文件中释放文件。
-c    创建新的档案文件。如果用户想备份一个目录或是一些文件，就要选择这个选项。
-r     把要存档的文件追加到档案文件的末尾。例如用户已经做好备份文件，又发现还有一个目录或
         是一些文件忘 记备份了，这时可以使用该选项，将忘记的目录或文件追加到备份文件中。
-t       列出档案文件的内容，查看已经备份了哪些文件。
-u      更新文件。就是说，用新增的文件取代原备份文件，如果在备份文件中找不到要更新的文件，
          则把它追加到备份文件的最后。

辅助选项：
-j         代表使用‘bzip2’程序进行文件的压缩    tar.bz2
-z       用gzip来压缩/解压缩文件，加上该选项后可以将档案文件进行压缩，但还原时也一定要使用该
            选项进行解压缩。   tar.gz
-v       详细报告tar处理的文件信息。如无此选项，tar不报告文件信息。
-b     该选项是为磁带机设定的，其后跟一数字，用来说明区块的大小，系统预设值为20（20×512 bytes）。
-f       使用档案文件或设备，这个选项通常是必选的。
-k       保存已经存在的文件。例如把某个文件还原，在还原的过程中遇到相同的文件，不会进行覆盖。
-m       在还原文件时，把所有文件的修改时间设定为。
-M      创建多卷的档案文件，以便在几个磁盘中存放。
-w           每一步都要求确认。