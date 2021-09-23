package com.hanshunlie.leecode;

public class MinStack {

    public Node[] stack;

    public Node min;
    public Node secondMin;

    //栈的大小
    public int size;

    //目前栈的位置
    public int num;

    /**
     * initialize your data structure here.
     */
    public MinStack() {
        size = 20;
        stack = new Node[size];


        num = -1;

    }

    public void push(int val) {
        if (num >= size)
            return;
        Node node = new Node(val);
        if (num < 0) {
            min = node;
        } else {
            if (val <= min.val) {
                secondMin = min;
                min = node;
                min.pre = secondMin;
            }
        }
        ++num;
        stack[num] = node;
    }

    public void pop() {
        if (num < 0)
            return;
        if (num == 0) {
            min = secondMin;
            num--;
            return;
        }
        if (stack[num].val <= min.val) {
            min = secondMin;
            secondMin = secondMin.pre;
        }
        num--;

    }

    public int top() {
        if (num < 0)
            return 0;

        return stack[num].val;
    }

    public int getMin() {
        return min.val;
    }


    class Node {
        int val;
        Node pre;
        Node after;

        Node() {
        }

        Node(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
//        minStack.push(2);
//        minStack.push(0);
//        minStack.push(3);
//        minStack.push(0);
//        minStack.getMin();
//        minStack.pop();
//        minStack.getMin();
//        minStack.pop();
//        minStack.getMin();
//        minStack.pop();
//        minStack.getMin();

        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        minStack.getMin();
        minStack.pop();
        minStack.top();
        minStack.getMin();
    }
}
