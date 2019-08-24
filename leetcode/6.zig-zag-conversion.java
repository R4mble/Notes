/*
 * @lc app=leetcode id=6 lang=java
 *
 * [6] ZigZag Conversion
 */
class Solution {
    public String convert(String s, int numRows) {
        char[] cs = s.toCharArray();
        int numsCols = 0;
        int duoyu = s.length() % (numRows * 2 - 2);
        if (duoyu > numRows) {
            numsCols = (s.length() / (numRows * 2 - 2)) * 2 + 1;
        } else {
            numsCols = (s.length() / (numRows * 2 - 2)) * 2 + 1;
        }
        int numsCols = 
    }
}

