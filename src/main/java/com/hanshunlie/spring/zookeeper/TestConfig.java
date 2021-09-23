package com.hanshunlie.spring.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfig {


    ZooKeeper zk;


    @Before
    public void conn() {
        zk = ZKUtils.getZk();
    }

    @After
    public void close() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getConf() {
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);

        MyConf myConf = new MyConf();
        watchCallBack.setMyConf(myConf);

        watchCallBack.await();
        //1-节点不存在
        //2-节点存在

        while (true){
            System.out.println(myConf.getConf());

        }
    }
}
