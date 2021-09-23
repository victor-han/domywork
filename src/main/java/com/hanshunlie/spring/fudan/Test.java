package com.hanshunlie.spring.fudan;


/**
 * 给定一个非负整数序列 x1, x2, …, xn，可以给每一个整数取负数或者取原值，
 * 求有多少种取法使得这些整数的和等于期望值 E
 *
 * 例子：
 * 输入：非负整数序列为 1, 1, 1, 1, 1，期望值 E 为 3
 * 输出 ：5 5 种取法分别为：
 * -1+1+1+1+1 = 3
 * 1-1+1+1+1 = 3
 * 1+1-1+1+1 = 3
 * 1+1+1-1+1 = 3
 * 1+1+1+1-1 = 3
 */
public class Test {

    int sum = 0;
    int count = 0;

    public void cal(int[] a, int target) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] < target) {
                //当小于目标值，则当前值可以取正，也可以取负数
            } else if (a[i] == target) {
                //当前值等于目标值，则可以直接
            } else if (a[i] > target) {
                //当大于目标值，则当前只能取负数
                sum += -a[i];
            }
        }
    }
}
