---
title: Java数据结构
date: 2019-07-08 11:43:30
tags:
---

ArrayList
    DEFAULT_CAPACITY
        默认容量: 10
    EMPTY_ELEMENTDATA
        共享空数组实例
    DEFAULTCAPACITY_EMPTY_ELEMENTDATA
        使用默认大小的空实例的共享空数组实例.
    elementData
        ArrayList的容量就是这个数组缓存的长度.
        任何elementData是DEFAULTCAPACITY_EMPTY_ELEMENTDATA的空ArrayList在第一个
        元素被添加时会被扩展到EMPTY_ELEMENTDATA.

    modCount
        这个列表被增加和删除的次数.

    size
        ArrayList包含元素的个数

    trimToSize()
        把列表的容量改为列表的当前大小.




LinkedList

HashMap

HashSet

TreeMap

TreeSet

HashTable
    使用synchronized保证线程安全.效率低下



    get

    put

    remove

LinkedBlockingQueue


二叉查找树
    节点数据大于左子树所有数据, 小于右子树所有数据.

    二分搜索, 链式结构, 项的插入和删除不必移动其他项.

    搜索:
```java
    public T search(T key) {
        if (tree.isEmpty()) {
            return null;
        }
        return recursiveSearch(tree, key).data;
    }

    BinaryTree<T> recursiveSearch(BinaryTree<T> root, T key) {
        if (root == null) return null;
        int c = key.compareTo(root.data);
        if (c == 0) return root;
        if (c < 0) return recursiveSearch(root.left, key);
        else return recursiveSearch(root.right, key); 
    }

```
    插入(非递归):
```java
    public void insert(T item) {
        if (tree.isEmpty()) {
            tree.makeRoot(data);
            size++;
            return;
        }

        BinaryTree<T> root = tree;
        boolean done = false;
        BinaryTree<T> newNode = null;
        while(!done) {
            int c = item.compareTo(root.data);
            if (c == 0) throw new OrderViolationException();
            if (c < 0) {
                if (root.left == null) {
                    newNode = new BinaryTree<T>();
                    root.left = newNode;
                    done = true;
                } else {
                    root = root.left;
                }
            } else {
                if (root.right == null) {
                    newNode = new BinaryTree<T>();
                    root.right = newNode;
                    done = true;
                } else {
                    root = root.right;
                }
            }
        }

        newNode.data = item;
        newNode.parent = root;
        size++;
    }
```
    删除:

```java
    BinaryTree<T> findPredecessor(BinaryTree<T> node) {
        if (node.left == null) {
            return null;
        }
        
    }


```
AVL树
    高度平衡的二叉查找树, 保证最坏情况搜索,插入和删除运行时间为O(log n)

