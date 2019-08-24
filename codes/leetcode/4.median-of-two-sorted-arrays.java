/*
 * @lc app=leetcode id=4 lang=java
 *
 * [4] Median of Two Sorted Arrays
 *
 * https://leetcode.com/problems/median-of-two-sorted-arrays/description/
 *
 * algorithms
 * Hard (26.54%)
 * Likes:    4439
 * Dislikes: 612
 * Total Accepted:    449K
 * Total Submissions: 1.7M
 * Testcase Example:  '[1,3]\n[2]'
 *
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * 
 * Find the median of the two sorted arrays. The overall run time complexity
 * should be O(log (m+n)).
 * 
 * You may assume nums1 and nums2 cannot be both empty.
 * 
 * Example 1:
 * 
 * 
 * nums1 = [1, 3]
 * nums2 = [2]
 * 
 * The median is 2.0
 * 
 * 
 * Example 2:
 * 
 * 
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 * 
 * The median is (2 + 3)/2 = 2.5
 * 
 * √ Accepted
 * √ 2084/2084 cases passed (2 ms)
 * √ Your runtime beats 100 % of java submissions
 * √ Your memory usage beats 90.69 % of java submissions (46.8 MB)
 * 看到这个成绩我人晕了, 我膨胀了, 不想看其他的解法了.
 * 我的解法: 先把两个数组合并, 再硬找.
 */
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length - 1;
        int n = nums2.length - 1;
        int k = nums1.length + nums2.length - 1;
        int p = nums1.length + nums2.length;
        int[] res = new int[nums1.length + nums2.length];

        while (m >= 0 && n>= 0) {
            if (nums1[m] > nums2[n]) {
                res[k] = nums1[m];
                k--;
                m--;
            } else {
                res[k] = nums2[n];
                k--;
                n--;
            }
        }
        while (m >= 0) {
            res[k] = nums1[m];
            k--;
            m--;
        }

        while (n >= 0) {
            res[k] = nums2[n];
            k--;
            n--;
        }

        if (p % 2 != 0) {
            return res[p / 2];
        } else {
            return (res[p / 2] + res[(p / 2) - 1]) / 2.0;
        }
    }
}

