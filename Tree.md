---
title: Tree
date: 2019-04-02 08:52:45
tags: [LeetCode,Tree]
categories: [算法]
---
说起树,首先想到的就是递归,递归,还有递归...
树的算法里确实有很多漂亮的递归,感觉比Scheme里面的递归还要迷人(主要是指迷惑的迷)

## 1. 树的高度
[104. Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/)
```Java
public int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
}
```
算法很优雅,但是并不能一眼看穿,来人肉单步执行一下吧
```Java
    3
   / \
  9  20
    /  \
   15   7

Math.max(maxDepth(9), maxDepth(20)) + 1
Math.max(Math.max(maxDepth(null), maxDepth(null)) + 1, Math.max(maxDepth(15), maxDepth(7)) + 1) + 1
Math.max(Math.max(0, 0) + 1, Math.max(Math.max(maxDepth(null), maxDepth(null)) + 1, Math.max(maxDepth(null), maxDepth(null)) + 1) + 1) + 1
Math.max(1, Math.max(1, 1) + 1) + 1
Math.max(1, 2) + 1
3
```

## 2. 平衡树
[110. Balanced Binary Tree](https://leetcode.com/problems/balanced-binary-tree)
```Java
    private boolean result = true;
    
    public boolean isBalanced(TreeNode root) {
        maxDepth(root);
        return result;
    }

    private int maxDepth(TreeNode root) {
        if (root == null) return 0;
        int l = maxDepth(root.left);
        int r = maxDepth(root.right);
        if (Math.abs(l - r) > 1)
            result = false;
        return Math.max(l, r) + 1;
    }
```
利用最大高度,每次递归求的时候看下左子树和右子树的高度差有没有超过1

## 3. 两节点的最长路径
[543. Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree)
```Java
    int max = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        depth(root);
        return max;
    }

    private int depth(TreeNode root) {
        if (root == null) return 0;
        int l = depth(root.left);
        int r = depth(root.right);
        max = Math.max(max, l + r);
        return Math.max(l, r) + 1;
    }
```
也是利用求最大高度的, 每次算一下左边和右边的距离, 存一个最大值.

## 4. 翻转树
[226. Invert Binary Tree](https://leetcode.com/problems/invert-binary-tree)

```Java
public TreeNode invertTree(TreeNode) {
    if (root == null) return null;
    TreeNode left = root.left;
    root.left = invertTree(root.right);
    root.right = invertTree(left);
    return root;
}
```

人肉单步执行:
```Java
     4
   /   \
  2     7
 / \   / \
1   3 6   9

invertTree(4)
    root = 4,
    left = 2,
    root.left = invertTree(7)
                    left = 6,
                    root.left = invertTree(9)
                                    left = null
                                    root.left = invertTree(null)
                                                    null
                                    root.right = invertTree(null)
                                                    null
                                    return root
                    root.right = invertTree(6)
                                    left = null
                                    root.left = invertTree(null)
                                                    null
                                    root.right = invertTree(null)
                                                    null
                                    return root
                    return root
    root.right = invertTree(2)
                    left = 1,
                    root.left = invertTree(3)
                                    left = null
                                    root.left = invertTree(null)
                                                    null
                                    root.right = invertTree(null)
                                                    null
                                    return root
                    root.right = invertTree(1)
                                    left = null
                                    root.left = invertTree(null)
                                                    null
                                    root.right = invertTree(null)
                                                    null
                                    return root
                    return root
            
    return root
```

交换值,本来是一个比较常见的操作:
int temp = a;
a = b;
b = temp;
但是要让这个操作应用到树的每一个左右节点时,就让人犯了难.
看上面的演算就知道要临场想出来难度有多大.
让我想到了以前背的四阶魔方最后一个L公式之后可能出现的中间两个错位现象,当时我们把它叫做牙齿公式.
15步的超长公式,很酷炫同时也很难记住.
我当时把这个公式用订书机钉在了数学笔记本上,每次翻笔记的时候都会顺带复习一遍.
但是难度大+用得少,不可避免的会遗忘.


## 5.合并树
[617. Merge Two Binary Trees](https://leetcode.com/problems/merge-two-binary-trees)
```Java
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) return null;
        if (t1 == null) return t2;
        if (t2 == null) return t1;
        TreeNode root = new TreeNode(t1.val + t2.val);
        root.left = mergeTrees(t1.left, t2.left);
        root.right = mergeTrees(t1.right, t2.right);
        return root;
    }
```
精准而优雅,嗯...    
新建了一个二叉树,而不是一个合并到另一个上.

## 6.