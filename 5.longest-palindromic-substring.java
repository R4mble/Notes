/*
 * @lc app=leetcode id=5 lang=java
 *
 * [5] Longest Palindromic Substring
 *
 * https://leetcode.com/problems/longest-palindromic-substring/description/
 *
 * algorithms
 * Medium (27.42%)
 * Likes:    3730
 * Dislikes: 356
 * Total Accepted:    576.2K
 * Total Submissions: 2.1M
 * Testcase Example:  '"babad"'
 *
 * Given a string s, find the longest palindromic substring in s. You may
 * assume that the maximum length of s is 1000.
 * 
 * Example 1:
 * 
 * 
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 * 
 * 
 * Example 2:
 * 
 * 
 * Input: "cbbd"
 * Output: "bb"
 * 
 * 考虑奇偶回文的差异.
 * 
 */

// √ Accepted
// √ 103/103 cases passed (5 ms)
// √ Your runtime beats 96.75 % of java submissions
// √ Your memory usage beats 99.95 % of java submissions (36.7 MB)

class Solution {

    int lo, maxLen;

    public String longestPalindrome(String s) {

        if (s.length() < 2) {
            return s;
        }

        //最后一个不用试了
        for (int i=0; i<s.length() - 1; i++) {
            extendPalindrome(s, i, i);
            extendPalindrome(s, i, i + 1);
        }
        return s.substring(lo, lo + maxLen);
    }

    private void extendPalindrome(String s, int j, int k) {
        while (j >= 0 && k < s.length() && s.charAt(j) == s.charAt(k)) {
            j--;
            k++;
        }
        //本来是k-j+1, 但是上面循环多跑了一遍, k多加了个1, j多减了个1, 所以k-1-(j+1)+1, 得到 k-j-1
        if (maxLen < k - j -1) {
            lo = j + 1;
            maxLen = k - j - 1;
        }
    }
}

