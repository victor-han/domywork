package com.hanshunlie.spring.zookeeper.lock;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RealWatchCallBack implements Watcher, AsyncCallback.StringCallback, AsyncCallback.ChildrenCallback, AsyncCallback.StatCallback {


    ZooKeeper zk;

    CountDownLatch cc = new CountDownLatch(1);

    String pathname;

    String threadName;

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }


    public void tryLock() {
//创建锁，ZooDefs.Ids.OPEN_ACL_UNSAFE 表示接入的权限，
        try {
            zk.create("/lock", "newdata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, "zxc");

            cc.await();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zk.delete("/" + pathname, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    //String callback
    //创建临时节点后，如果name不为空，表示创建成功。需要开始监控目前是不是最小的节点。
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name != null) {
            pathname = name;
            zk.getChildren("/", false, this, "ghj");

        }
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {

            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/", false, this, "ghj");


                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }


    //children callback
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        Collections.sort(children);
        int i = children.indexOf(path.substring(1));
        if (i < 1) {
            //为第一个节点，获得锁
            System.out.println(threadName + "get lock, lock name : " + path.substring(1));
            cc.countDown();
        } else {
            //不是第一个节点，需要监控前一个节点
            zk.exists("/" + children.get(i - 1), this, this, "112");

        }

    }

    //statCallBack
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }
}
