package com.hanshunlie.leecode;

import java.util.HashMap;

public class LRUCache {

    DoubleLinkedList list;

    HashMap<Integer, Node> map;

    int cap;

    public LRUCache(int capacity) {
        this.cap = capacity;
        map = new HashMap(capacity);
        list = new DoubleLinkedList();
    }

    public int get(int key) {
        Node node = map.get(key);
        if (null == node) {
            return -1;
        }
        int val = map.get(key).val;
        //把该节点放到最前面
        list.addFirst(node);
        return val;
    }

    public void put(int key, int value) {
        Node node = map.get(key);

        //原先如果已经存在，只用覆盖
        if (null != node){
            node.val= value;
            list.addFirst(node);
            return;
        }

        //如果不存在

        //移除最后一个
        if (map.size() == cap) {
            int i = list.deleteLast();
            map.remove(i);
        }
        //插入这个节点
        node = new Node(key, value);
        map.put(key, node);

        list.addFirst(node);
    }


    class DoubleLinkedList {

        //head 刚使用
        public Node head;
        //tail 最久未使用
        public Node tail;

        public DoubleLinkedList() {
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
//            head.prev = tail;

            tail.prev = head;
//            tail.next = head;
        }

        public int addFirst(Node node) {
            //原先节点如果已经在缓存中存在，需要移除掉，移到最前面
            if (node.next != null && node.prev != null) {
                //原先的前后节点连起来
                Node prev = node.prev;
                Node next = node.next;
                prev.next = next;
                next.prev = prev;
            }
            //该节点放在第一个
            Node nextBefore = head.next;

            node.next = nextBefore;
            node.prev = head;

            head.next = node;
            nextBefore.prev=node;
            return node.key;

        }

        public int deleteLast() {
//            if (head.next == tail) return -1;

            Node deleteNode = tail.prev;
            Node prev = deleteNode.prev;
            prev.next=tail;
            tail.prev = prev;

            return deleteNode.key;
        }

    }

    class Node {
        public int key;
        public int val;
        public Node prev;
        public Node next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }


    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.get(1);
        lruCache.put(3,3);
        lruCache.get(2);
        lruCache.put(4,4);
        lruCache.get(1);
        lruCache.get(3);
        lruCache.get(4);
    }
}
