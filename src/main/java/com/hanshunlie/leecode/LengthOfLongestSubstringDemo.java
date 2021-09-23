package com.hanshunlie.leecode;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 无重复最长子串
 */
public class LengthOfLongestSubstringDemo {


    public int lengthOfLongestSubstring(String s) {
        char[] chars = s.toCharArray();
        if (chars.length==1){
            return 1;
        }
        Set<Character> set= new HashSet<>();
        int n =0;
        int biggestSize =0;

        for (int i = 0; i < chars.length; i++) {

        }

        return biggestSize;
    }

    public static void main(String[] args) {
        String a = "au";
        LengthOfLongestSubstringDemo demo = new LengthOfLongestSubstringDemo();
        int i = demo.lengthOfLongestSubstring(a);
        System.out.println(i);
    }
}
