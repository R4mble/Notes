---
title: 精通Redis系列 (0) - 写在前面的话
date: 2019-09-19 17:42:45
categories: [Redis]
---
Simple Dynamic Strings, SDS
存储字符串和整型数据, 兼容C语言标准字符串处理函数, 在此基础之上保证二进制安全, 避免缓冲区溢出.

### 什么是二进制安全?
C语言中用"\0"表示字符串的结束, 如果字符串本身就含有"\0"字符, 字符串就会被截断, 就是非二进制安全.
所以C字符串不能保存图片,音频,视频,压缩文件这样的二进制数据.

如果可以通过某种机制, 保证读写字符串时不损害其内容, 则称为二进制安全.
Redis的sds有长度统计变量len, 读写字符串不依赖"\0"终止符,保证了二进制安全.


### 结构
1. Redis3.2之前
```c
struct sds {

    // buf中已占用空间
    int len;

    // buf中剩余可用字节数
    int free;

    // 数据空间, 柔性数组, 通过malloc函数动态分配内存
    char buf[];
}
```
64位系统中: len与free各占4字节.

- 预分配冗余空间的方式来减少内存的频繁分配.字符串
    长度小于1M时,加倍扩容,大于1M时每次扩1M.最大长度为512M.
- 惰性空间释放
  当sds要缩短保存的字符串时,不立即使用内存重分配来回收缩短后多出来的字节,而是使用free记录下来.

2. 改进版sds

```c
/* Note: sdshdr5 is never used, we just access the flags byte directly.
 * However is here to document the layout of type 5 SDS strings. */
struct __attribute__ ((__packed__)) sdshdr5 {
    unsigned char flags; /* 3 lsb of type, and 5 msb of string length */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr8 {
    uint8_t len; /* used */
    uint8_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr16 {
    uint16_t len; /* used */
    uint16_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr32 {
    uint32_t len; /* used */
    uint32_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr64 {
    uint64_t len; /* used */
    uint64_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
```
一般情况下结构体会按其所有变量大小的最小公倍数做字节对齐,而使用packed修饰后,结构体变为按照1字节对齐.
好处: 1. 节省内存; 2. 实现简单--SDS返回给上层指向内容的指针而不是结构体首地址.

### SDS的创建
sdsnewlen函数创建SDS
```c
sds sdsnewlen(const void *init, size_t initlen) {
    void *sh;
    sds s;
    char type = sdsReqType(initlen);
    /* Empty strings are usually created in order to append. Use type 8
     * since type 5 is not good at this. */
    if (type == SDS_TYPE_5 && initlen == 0) type = SDS_TYPE_8;
    int hdrlen = sdsHdrSize(type);
    unsigned char *fp; /* flags pointer. */

    sh = s_malloc(hdrlen+initlen+1);
    if (init==SDS_NOINIT)
        init = NULL;
    else if (!init)
        memset(sh, 0, hdrlen+initlen+1);
    if (sh == NULL) return NULL;
    s = (char*)sh+hdrlen;
    fp = ((unsigned char*)s)-1;
    switch(type) {
        case SDS_TYPE_5: {
            *fp = type | (initlen << SDS_TYPE_BITS);
            break;
        }
        case SDS_TYPE_8: {
            SDS_HDR_VAR(8,s);
            sh->len = initlen;
            sh->alloc = initlen;
            *fp = type;
            break;
        }
        case SDS_TYPE_16: {
            SDS_HDR_VAR(16,s);
            sh->len = initlen;
            sh->alloc = initlen;
            *fp = type;
            break;
        }
        case SDS_TYPE_32: {
            SDS_HDR_VAR(32,s);
            sh->len = initlen;
            sh->alloc = initlen;
            *fp = type;
            break;
        }
        case SDS_TYPE_64: {
            SDS_HDR_VAR(64,s);
            sh->len = initlen;
            sh->alloc = initlen;
            *fp = type;
            break;
        }
    }
    if (initlen && init)
        memcpy(s, init, initlen);
    s[initlen] = '\0';
    return s;
}
```
### 释放字符串
```c
void sdsfree(sds s) {
    if (s == NULL) return;
    s_free((char*)s-sdsHdrSize(s[-1]));
}

// 将len归零, 新的数据可以覆盖写
void sdsclear(sds s) {
    sdssetlen(s, 0);
    s[0] = '\0';
}
```