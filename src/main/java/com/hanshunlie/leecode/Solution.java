package com.hanshunlie.leecode;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    //全局最大和
    private int maxVal = Integer.MIN_VALUE;

    /**
     * 二叉树中的最大路径和
     */

    public int maxPathSum(TreeNode root) {
        postOrder(root);

        return maxVal;
    }

    /**
     * 拿到一边，包括该root的最大sum
     *
     * @param root
     * @return
     */
    public int postOrder(TreeNode root) {

        if (root==null){
            return 0;
        }
        //后续遍历
        int left = Math.max(postOrder(root.left), 0);
        int right = Math.max(postOrder(root.right), 0);

        //选定 左中右 三个节点相加
        int maxTreeSum = root.val + left + right;

        //更新一下全局最大和。比较当前的最大和  与  左中右相加的和哪个大
        maxVal = Math.max(maxVal, maxTreeSum);

        //选定一个分支往上走
        return root.val + Math.max(left, right);
    }


    List<Integer> rightView = new ArrayList<>();
    /**
     * 二叉树的右视图
     * @param root
     * @return
     */
    public List<Integer> rightSideView(TreeNode root) {
        getRight(root);

        return rightView;
    }

    public void getRight(TreeNode root){
        if (root == null)
            return;

        if (root.right!=null){
            rightView.add(root.val);
            getRight(root.right);
        }

        if (root.right == null && root.left!=null){
            rightView.add(root.val);
            getRight(root.left);
        }

        if (root.right == null && root.left == null){
            rightView.add(root.val);
        }

    }


    //内部类
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
