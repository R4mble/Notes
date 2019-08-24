---
title: Weekly Contest 130
date: 2019-04-02 07:01:37
tags: [LeetCode,二进制操作]
categories: [LeetCode Weekly Contest,算法]
---
# [1018. Binary Prefix Divisible By 5](https://leetcode.com/problems/binary-prefix-divisible-by-5/)

### 我想写对个东西怎么就这么难 -_-
------

1. 最开始的初始暴力想法: 把每个位置对应的数转化为题目中要求的数, 然后看是否能被5整除.
    简单的写了个双重循环后开始脑壳疼, 数组头部的边界条件不太好写啊, 不要然从后往前遍历.
    过于复杂了,看看别人的思路吧.
2. 这个规则是数组元素往二进制里面加, 思路就是就用左移并且把数组元素是0或1考虑进来.
3. 
    ```Java
        List<Boolean> res = new ArrayList<>();
        int acc = 0;
        for (int i : A) {
            acc = (acc << 1) + i;
            res.add(acc % 5 == 0);
        }
        return res;
    ```
    按照别人的python解法仿照写的java代码,给的几个Example都没问题,点击Submit,失败.
    这种写法的确是符合正常思路,但是题目中给的数组长度是1到30000,而java的int只有2^31,
    所以int溢出了.
    python还是爽啊,写代码跟着思路走就行,你只管岁月静好,自有动态类型替你负重前行.
    不过倔强的java程序员不会就此认输,悄悄地把取模移了上去: 
    ```Java
        List<Boolean> res = new ArrayList<>();
        int acc = 0;
        for (int i : A) {
            acc = (acc * 2 + i) % 5;
            res.add(acc == 0);
        }
        return res;
    ```
    为什么敢移上去呢?就因为int装不下吗?并不:

    ```Java
    证明 (n * 2 + 1) % 5 等于 ((n % 5) * 2 + 1) % 5
    因为
        (a + b) % p = (a % p + b % p) % p
        (a * b) % p = (a % p * b % p) % p
        a % p == a % p % p
    所以
        左边 = ((n * 2) % 5 + 1 % 5) % 5
            = ((((n % 5) * 2) % 5) + 1) % 5

        右边 = 左边
    ```

    嗯...不知道这儿又可以引发多少对两种语言的思考.

4. 写java的过程中同时又踩了其他的坑-----运算符优先级:
    乘除取模 > 加减 > 移位 > 按位或
    所以 
        ```Java
        acc << 1 + a 问题很大
        acc << 1 | a 还ok
        acc << 1 | a % 5 问题很大
        ```
    多写括号可破:
        ```Java
        ((acc << 1) | a) % 5
        ```
5. 从移位来考虑应当是 acc << 1 | a
    从二进制转十进制的阶数来考虑是 acc * 2 + 1
    两种写法杂糅在一起感觉有些奇怪

# [1017. Convert to Base -2](https://leetcode.com/contest/weekly-contest-130/problems/convert-to-base-2/)
考虑十进制到二进制的转换:
```java
public String base2(int N) {
    String res = ";
    while (N != 0) {
        res = Integer.toString(N & 1) + res;
        N = N >> 1;
    }
    return res == "" ? "0" : res;
}
```
每次把最后一位二进制数留下来,组成一个二进制字符串.
对于负二进制:
```java
public String baseNeg2(int N) {
    String res = "";
    while (N != 0) {
        res = Integer.toString(N & 1) + res;
        N = -(N >> 1);
    }
    return res == "" ? "0" : res;
}
```
对于String的优化:
```java
public String baseNeg2(int N) {
    StringBuilder res = new StringBuilder();
    while (N != 0) {
        res.insert(0, Integer.toString(N & 1));
        N = -(N >> 1);
    }
    return res.toString().equals("") ? "0" : res.toString();
}
```

# [1019. Next Greater Node In Linked List](https://leetcode.com/contest/weekly-contest-130/problems/next-greater-node-in-linked-list/)
考查对于链表,栈,数组,线性表的组合使用.

```
先把链表的东西都存到线性表来.
遍历线性表的同时
    若栈不为空 且 当前遍历的线性表的值a大于栈顶索引对应的值b,
        则将要返回的数组的a项设置为b,同时出栈.
    否则
        把当前遍历的索引入栈.
```         

```java
public int[] nextLargerNodes(ListNode head) {
    ArrayList<Integer> list = new ArrayList<>();
    while (head != null) {
        list.add(head.val);
        head = head.next;
    }
    int[] res = new int[list.size()];
    Stack[Integer] stack = new Stack<>();
    for (int i=0; i<list.size(); i++) {
        while (!stack.isEmpty() && list.get(i) > list.get(stack.peek())) {
            res[stack.pop()] = list.get(i);
        }
        stack.push(i);
    }
    return res;
}        
```

# [1020. Number of Enclaves](https://leetcode.com/contest/weekly-contest-130/problems/number-of-enclaves/)
神奇的dfs,关键在于构造递归结构,入参,返回值以及终止条件.
1. 遍历整个二维数组,只对在边上的行列进行dfs
2. 将可以被边上索引到的1都变为0.
3. 统计二维数组中剩余的1.
```java
public int numEnclaves(int[][] A) {
    for (int i=0; i<A.length; i++) {
        for (int j=0; j<A[0].length; j++) {
            if (i==0 || j==0 || i==A.length-1 || j==A[0].length-1) {
                dfs(A, i, j);
            }
        }
    }
    
    int result = 0;
    for (int i=0; i<A.length; i++) {
        for (int j=0; j<A[0].length; j++) {
            if (A[i][j] == 1) {
                result++;
            }
        }
    }
    return result;
}

public void dfs(int[][] a, int i, int j) {
    if (i>=0 && i<a.length && j>=0 && j<a[0].length && a[i][j]==0) {
        a[i][j] = 0;
        dfs(a, i+1, j);
        dfs(a, i-1, j);
        dfs(a, i, j+1);
        dfs(a, i, j-1);
    }
}
```