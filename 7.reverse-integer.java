/*
 * @lc app=leetcode id=7 lang=java
 *
 * [7] Reverse Integer
 *
 * https://leetcode.com/problems/reverse-integer/description/
 *
 * algorithms
 * Easy (25.35%)
 * Likes:    2210
 * Dislikes: 3392
 * Total Accepted:    713.4K
 * Total Submissions: 2.8M
 * Testcase Example:  '123'
 *
 * Given a 32-bit signed integer, reverse digits of an integer.
 * 
 * Example 1:
 * 
 * 
 * Input: 123
 * Output: 321
 * 
 * 
 * Example 2:
 * 
 * 
 * Input: -123
 * Output: -321
 * 
 * 
 * Example 3:
 * 
 * 
 * Input: 120
 * Output: 21
 * 
 * 
 * Note:
 * Assume we are dealing with an environment which could only store integers
 * within the 32-bit signed integer range: [−2^31,  2^31 − 1]. For the purpose
 * of this problem, assume that your function returns 0 when the reversed
 * integer overflows.
 * 
 */
class Solution {
    /**
     * √ Your runtime beats 27.55 % of java submissions
     * √ Your memory usage beats 14.72 % of java submissions (33.7 MB)
     * 啊, 有够丑陋的.
     */
    
    public int reverseUgly(int x) {
        char[] arr = String.valueOf(x).toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean zero = true;
        for (int i=arr.length-1; i>0; i--) {
            if (zero) {
                if (arr[i] == 0) {
                    continue;
                } else {
                    zero = false;
                }
            }

            sb.append(arr[i]);
        }
        if (arr[0] == '-') {
            sb.insert(0, '-');
        } else {
            sb.append(arr[0]);
        }

        int res = 0;
        try {
            res = Integer.parseInt(sb.toString());
        } catch (Exception e) {
            return 0;
        }
        return res;
    }

    /**
     *  √ Your runtime beats 100 % of java submissions
     *  √ Your memory usage beats 12.22 % of java submissions (33.5 MB)
     *  这个为啥这么吃内存
     */
    public int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int tail = x % 10;
            int newResult = result * 10 + tail;
            if ((newResult - tail) / 10 != result) return 0;
            result = newResult;
            x = x / 10;
        }
        return result;
    }
}

