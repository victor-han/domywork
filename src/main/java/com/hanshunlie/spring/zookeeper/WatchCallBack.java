package com.hanshunlie.spring.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {


    ZooKeeper zk;
    MyConf myConf;
    CountDownLatch cc = new CountDownLatch(1);

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public MyConf getMyConf() {
        return myConf;
    }

    public void setMyConf(MyConf myConf) {
        this.myConf = myConf;
    }

    public void await(){
        zk.exists("/AppConf", this, this, "ABC");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if (data!=null){
            String s = new String(data);
            myConf.setConf(s);
            cc.countDown();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (stat!= null){
            zk.getData("/AppConf", this,this, "asdf");
        }
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()){

            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf", this,this, "asdf");

                break;
            case NodeDeleted:
                //节点删除，其实可以不关心

                break;
            case NodeDataChanged:
                zk.getData("/AppConf", this,this, "asdf");

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
}
