package com.hanshunlie.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ThreeSum {

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);

        List<List<Integer>> finalResult = new ArrayList();
        //去重
        Set<List<Integer>> filter = new HashSet<List<Integer>>();

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (num>0) break;

            //j为左指针，k为右指针
            int j = i+1, k = nums.length-1;
            while (j<k){
                if (nums[j] + nums[k] == -num) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    if (!filter.contains(list)){
                        finalResult.add(list);
                        filter.add(list);
                    }

                    j++;k--;
                    continue;
                }

                if (nums[j]+nums[k] < -num ){
                    j++;
                }
                if (nums[j]+nums[k] > -num){
                    k--;
                }
            }

        }
        return finalResult;

    }

    public static void main(String[] args) {
        int[] nums = {-2,0,1,1,2};
        List<List<Integer>> lists = threeSum(nums);
        for (int i = 0; i < lists.size(); i++) {
            List<Integer> integers = lists.get(i);
            integers.stream().forEach(a-> System.out.println(a));
        }
    }


}
