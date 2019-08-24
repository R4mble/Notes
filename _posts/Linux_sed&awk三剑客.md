---
title: Linux
date: 2019-06-29 17:55:26
tags: [Linux]
categories: [Linux]
---

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