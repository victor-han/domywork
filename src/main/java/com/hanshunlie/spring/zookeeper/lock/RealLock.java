package com.hanshunlie.spring.zookeeper.lock;

import com.hanshunlie.spring.zookeeper.ZKUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RealLock {

    ZooKeeper zk;

    String pathname;

    @Before
    public void before() {

        zk = ZKUtils.getZk();

    }

    @After
    public void after() {
        try {
            zk.delete("/" + pathname, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {

                    WatchCallBack watchCallBack = new WatchCallBack();
                    watchCallBack.setZk(zk);
                    watchCallBack.setThreadName(Thread.currentThread().getName());

                    watchCallBack.tryLock();

                    //获取资源，开始执行
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //结束执行
                    watchCallBack.unLock();
                }
            }.start();
        }

        while (true) {

        }
    }


}
