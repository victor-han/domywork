package com.hanshunlie.leecode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class rightSideViewDemo {
    List<Integer> result = new ArrayList<>();

    public List<Integer> rightSideView(TreeNode root) {
        bfs(root);
        dfs(root);
        return result;
    }

    public List<Integer> bfs(TreeNode root) {
        if (null == root) {
            return result;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode poll = queue.poll();
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }
                if (i == size - 1) {
                    result.add(poll.val);
                }
            }
        }
        return result;
    }


    public List<Integer> dfs(TreeNode root) {
        if (null == root) {
            return result;
        }
        Stack<TreeNode> nodeStack = new Stack<>();
        nodeStack.push(root);

        Stack<Integer> depthStack = new Stack<>();
        depthStack.push(1);

        while (!nodeStack.isEmpty()) {
            TreeNode popNode = nodeStack.pop();
            Integer popDepth = depthStack.pop();
            if (popDepth > result.size()){
                result.add(popNode.val);
            }
            if (popNode.left != null) {
                nodeStack.push(popNode.left);
                depthStack.push(popDepth + 1);
            }
            if (popNode.right != null) {
                nodeStack.push(popNode.right);
                depthStack.push(popDepth + 1);
            }

        }

        return result;
    }

    public static void main(String[] args) {

    }
}
