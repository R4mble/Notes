---
title: 精通Redis系列 (2) - 跳跃表
date: 2019-09-19 17:42:45
categories: [Redis]
---
核心思想: 有序链表分层, 空间换时间
性质:
1. 头结点中有一个64层结构, 每层结构包含指向本层的下个节点的指针, 指向本层下个节点中间所跨越的节点个数为本层的跨度.
2. 除头结点外, 层数最多的节点的层高为跳跃表的高度,
3. 每一层都是有序链表, 数据递增
4. 除头结点外, 一个元素在上层有序链表中出现, 一定会在下层有序链表中出现.
5. 跳跃表每层最后一个节点指向Null, 表示本层有序链表的结束
6. 跳跃表有一个tail指针, 指向跳跃吧的最后一个节点.
7. 最底层的有序链表包含所有节点, 最底层的节点个数为跳跃表的长度(不包括头节点)
8. 每个节点包含一个后退指针, 头结点和第一个节点指向Null

## 冲向源码
```c
typedef struct zskiplistNode {
    sds ele;
    // 用于存储排序的分值
    double score;
    // 回退指针
    struct zskiplistNode *backward;
    // 柔性数组,每个节点的数组长度不一样. 生成跳跃表节点时,随机生成一个1-64的值,值越大出现的概率越低
    struct zskiplistLevel {
        // 本层的下一个节点, 尾节点的forward指向Null
        struct zskiplistNode *forward;
        // forward指向的节点与本节点之间的元素个数, span值越大,跳过的节点个数越多
        unsigned long span;
    } level[];
} zskiplistNode;

typedef struct zskiplist {
    // header: 头结点,level数组元素个数为64, 头结点不存储任何member和score,ele为Null,score为0,
    // 不计入跳跃表的总长度.头结点在初始化时,64个元素都指向Null,span值都为0
    struct zskiplistNode *header, *tail;
    unsigned long length;
    int level;
} zskiplist;

// 每个节点的ele存储zset的member, socre存储score, 当有序集合分值相同时,节点会按member的字典序进行排序
typedef struct zset {
    dict *dict;
    zskiplist *zsl;
} zset;
```

### 跳跃表的创建
#### 节点层高
最小值为1, 最大值为64.
生成算法: level初始为1, 随机值小于p时level加1
    level为1的概率: 1-p
    level为2的概率: p(1-p)
    level为3的概率: (1-p) * p^2

节点层高为n的概率是(1-p) * p^(n-1), p=0.25
节点的期望层高为1/(1-p), 即: 1.33

#### 创建节点
每个节点就是有序集合的一个元素, 创建节点时, 待创建节点的层高,分值,member等都已确定.
```c
zskiplistNode *zslCreateNode(int level, double score, sds ele) {
    zskiplistNode *zn =
        zmalloc(sizeof(*zn)+level*sizeof(struct zskiplistLevel));
    zn->score = score;
    zn->ele = ele;
    return zn;
}

/* Create a new skiplist. */
zskiplist *zslCreate(void) {
    int j;
    zskiplist *zsl;

    zsl = zmalloc(sizeof(*zsl));
    zsl->level = 1;
    zsl->length = 0;
    zsl->header = zslCreateNode(ZSKIPLIST_MAXLEVEL,0,NULL);
    for (j = 0; j < ZSKIPLIST_MAXLEVEL; j++) {
        zsl->header->level[j].forward = NULL;
        zsl->header->level[j].span = 0;
    }
    zsl->header->backward = NULL;
    zsl->tail = NULL;
    return zsl;
}
```

#### 插入节点
1. 查找要插入的位置
2. 调整跳跃表高度
3. 插入节点
4. 调整backward

```c
/* Insert a new node in the skiplist. Assumes the element does not already
 * exist (up to the caller to enforce that). The skiplist takes ownership
 * of the passed SDS string 'ele'. */
zskiplistNode *zslInsert(zskiplist *zsl, double score, sds ele) {

    // 插入节点时, 需要更新被插入节点每层的前一个节点. 将每层需要更新的节点记录在update[i]中
    // x是跳跃表的头结点
    zskiplistNode *update[ZSKIPLIST_MAXLEVEL], *x;
    
    // 记录当前层从header节点到update[i]节点所经历的步长, 在更新update[i]的span和设置
    // 新插入节点的span时用到
    unsigned int rank[ZSKIPLIST_MAXLEVEL];
    int i, level;

    serverAssert(!isnan(score));
    x = zsl->header;
    for (i = zsl->level-1; i >= 0; i--) {
        /* store rank that is crossed to reach the insert position */
        rank[i] = i == (zsl->level-1) ? 0 : rank[i+1];
        while (x->level[i].forward &&
                (x->level[i].forward->score < score ||
                    (x->level[i].forward->score == score &&
                    sdscmp(x->level[i].forward->ele,ele) < 0)))
        {
            rank[i] += x->level[i].span;
            x = x->level[i].forward;
        }
        update[i] = x;
    }
    /* we assume the element is not already inside, since we allow duplicated
     * scores, reinserting the same element should never happen since the
     * caller of zslInsert() should test in the hash table if the element is
     * already inside or not. */
    level = zslRandomLevel();
    if (level > zsl->level) {
        for (i = zsl->level; i < level; i++) {
            rank[i] = 0;
            update[i] = zsl->header;
            update[i]->level[i].span = zsl->length;
        }
        zsl->level = level;
    }
    x = zslCreateNode(level,score,ele);
    for (i = 0; i < level; i++) {
        x->level[i].forward = update[i]->level[i].forward;
        update[i]->level[i].forward = x;

        /* update span covered by update[i] as x is inserted here */
        x->level[i].span = update[i]->level[i].span - (rank[0] - rank[i]);
        update[i]->level[i].span = (rank[0] - rank[i]) + 1;
    }

    /* increment span for untouched levels */
    for (i = level; i < zsl->level; i++) {
        update[i]->level[i].span++;
    }

    x->backward = (update[0] == zsl->header) ? NULL : update[0];
    if (x->level[0].forward)
        x->level[0].forward->backward = x;
    else
        zsl->tail = x;
    zsl->length++;
    return x;
}
```

SkipList 有序数据结构
    在每个节点维持多个指向其他节点的指针.
    平均O(logN),最坏O(N)复杂度的节点查找,可以通过顺序性操作来
        批量处理节点.
    当有序集合包含的元素数量较多, 或有序集合中元素的member是比较长的字符串时,
    Redis使用跳表来作为有序集合键的底层实现.

跳表在Redis的两个应用: 有序集合键, 集群节点中做内部数据结构


zskiplist 
    header: 指向跳表的表头节点
    tail: 指向跳表的表尾节点
    level: 记录目前跳表内,层数最大的那个节点的层数(表头节点的层数不计算在内)
    length: 跳表目前包含节点的数量(表头节点不计算在内)

zskiplistNode
    level: L1代表第一层, L2代表第二层.