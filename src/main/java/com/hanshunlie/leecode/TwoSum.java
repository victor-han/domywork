package com.hanshunlie.leecode;

import sun.jvmstat.perfdata.monitor.PerfStringVariableMonitor;

import java.util.HashMap;

public class TwoSum {

    public static int[] twoSum(int[] nums, int target) {
        HashMap map = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++) {
            Integer target2 = target - nums[i];
            Integer temp = (Integer) map.get(target2);
            if (temp!=null && temp > 0 && temp!= i) {
                result[0] = i;
                result[1] = temp;
                return result;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = {1,3, 4, 2};
        int[] ints = twoSum(nums, 6);
        for (int a :
                ints) {
            System.out.println(a);
        }
    }

}
