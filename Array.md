---
title: Array
date: 2019-04-02 17:55:26
tags: [LeetCode, Array]
categories: [算法]
---
1. [283. Move Zeroes](https://leetcode.com/problems/move-zeroes/)
遍历数组时用一个索引idx存非零元素往前填充的位置
```Java
public void moveZeros(int nums) {
    int idx = 0;
    for (int num : nums) {
        if (num != 0) {
            nums[idx++] = num;
        }
    }
    while (idx < nums.length) {
        nums[idx++] = 0;
    }
}
```
2.[566. Reshape the Matrix](https://leetcode.com/problems/reshape-the-matrix/)
