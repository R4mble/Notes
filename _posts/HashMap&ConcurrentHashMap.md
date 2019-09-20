---
title: HashMap
date: 2019-06-29 17:55:26
categories: [数据结构与算法]
---

HashMap
   数组加链表
     初始容量 16            必须是2的n次方
     负载因子 0.75          容量达到0.75时进行扩容resize(), 16 * 0.75 = 12, 存入第12个值时HashMap会执行扩容
     树形化阈值 8           链表长度大于等于8时转化为红黑树.
     解除树形化阈值 6
     树形化的另一条件 Map数组长度阈值 64    当数组长度小于这个值时, 就算树形化阈值达标, 链表也不会转化为红黑树, 而是优先扩容数组resize()
     内部数组 Node<K,V>[] table;
     数组扩容阈值  总容量 * 负载因子,  扩容为总容量的两倍.

```java
// Node节点
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next; //下一个节点
}


// 扩容
if (++size > threshold)
    resize();

// hash算法
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

// 对hash值的模运算, 数组长度减一后与hash值按位与.
if ((p = tab[i = (n - 1) & hash]) == null)

// onlyIfAbsent: 如果为true, 不修改已存在的value
// evict: 如果为false, 表处于创建模式, 用于子类LinkedHashMap
public V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    // 内部数组
    HashMap.Node<K,V>[] tab;
    // hash对应的索引位中的首节点
    HashMap.Node<k,V> p;
    // n: 内部数组长度, i: hash对应的索引位
    int n, i;

    // 首次put时, 内部数组为空, 扩充数组.
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;

    // 根据hash值计算数组索引, 获取该索引位置的首节点, 如果为null, 添加一个新的节点
    if ((p = tab[i = (n - 1) & hash]) == null) 
        tab[i] = newNode(hash, key, value, null);
    else {
        HashMap.Node<K,V> e; K k;
        // 如果首节点的key和要存入的key相同, 那么直接覆盖value的值
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 如果首节点是红黑树节点, 将键值对添加到红黑树
        else if (p instanceof HashMap.TreeeNode) 
            e = (HashMap.TreeNode<K,V>p).putTreeVal(this, tab, hash, key, value);
        else {
            for (int binCount = 0; ++binCount) {
                // 到达链表末尾, 添加新节点, 如果长度足够, 尝试转换为树结构
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        treeifyBin(tab, hash);
                    break;
                }

                // 检查链表中是否已经包含key
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }

        // 覆盖value
        if (e != null) {
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null) 
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }

    // 快速失败机制
    ++modCount;

    // 扩容
    if (++size > threshold) 
        resize();
    afterNodeInsertion(evict);
    return null;
}


```
    Node是单向链表节点, Entry是双向链表节点, TreeNode是红黑树节点.
    HashMap是基于拉链法实现的一个散列表, 内部由数组和链表和红黑树实现.

      容量以2的次方扩充,可以使用位运算代替模运算.
       x / 2^n 相当于把x右移n位, 得到x / 2^n的商, 被移掉的n位就是 x % 2^n,
        即x / 2^n 的余数.

      x & (2^n -1),  2^n的二进制就是1后面跟n个0, (2^n - 1) 就是0后面跟n个1,
      x和(2^n - 1) 按位与, 就相当于取x的二进制的后n位.
        测试了一下, 跑10亿次快0.3秒.


    hashCode()方法返回32位整数. hash()方法对hashCode的高16位和低16位进行异或,
        试图减少hash冲突.

    红黑树的平均查找长度是log(n), 长度为8时平均查找长度为log(8)=3, 
    链表的平均查找长度是n/2, 长度为8时平均查找长度为8/2=4.
    使用6和8作为平衡点, 防止链表和数频繁转换.

```java
// resize()数组扩容
final HashMap.Node<K,V>[] resize() {
    HashMap.Node<K,V> oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    itn newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < )
    }
}
```
   
ConcurrentHashMap
    分段锁.
    跨段方法: size()和containsValue()
        需要锁定整个表, 按顺序锁定所有段, 操作完毕后, 按顺序释放所有段的锁.

    由Segment数组结构和HashEntry数组结构组成, Segment是一种可重入锁ReentrantLock,
    一个ConcurrentHashMap包含一个Setment数组, 一个Setment里包含一个HashEntry数组,
    每个HashEntry是一个链表结构的元素, 每个Segment守护一个HashEntry里的元素, 当对HashEntry
    里的数据进行修改时, 必须先获得它的Setment锁.
