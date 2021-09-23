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

public class WatchCallBack implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    ZooKeeper zk;

    String threadName;

    CountDownLatch cc = new CountDownLatch(1);

    String pathName;

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
        try {
            zk.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, "abc");

            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zk.delete(pathName, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent event) {
        //如果是第一个节点释放，只有第二个节点收到回调事件
        //如果不是第一个人挂掉，而是某一个挂掉，也能造成后面的节点收到回调事件
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                //节点删除，就监控根目录，
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


    //StringCallback
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        //如果创建成功，name不为空，创建的节点名称
        if (name != null) {
            System.out.println(threadName + " create node : " + name);
            pathName = name;
            zk.getChildren("/", false, this, "DFGH");
        }
    }


    //getChildren call back,Children2Callback
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        //能进到这个方法肯定是创建完成了，一定能看到自己前面的节点

        if (children == null) {
            System.out.println(ctx.toString() + "list null ");
            return;
        }

        Collections.sort(children);
        int i = children.indexOf(pathName.substring(1));
        //是不是第一个节点
        if (i == 0) {
            try {
                System.out.println(threadName + " i am first ...，lock num ： " + pathName.substring(1));
                //为了让第一个线程干活慢一点。
                zk.setData("/", threadName.getBytes(), -1);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cc.countDown();
        } else {
            //no
            //拿到前一个节点,需要回调，因为可能获得锁失败
            zk.exists("/" + children.get(i - 1), this, this, "fghj");
        }


    }

    //StatCallback
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }
}
