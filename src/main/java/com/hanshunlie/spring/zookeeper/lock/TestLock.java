package com.hanshunlie.spring.zookeeper.lock;

import com.hanshunlie.spring.zookeeper.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLock {

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
    public void lock() {

        for (int i = 0; i < 10; i++) {

            new Thread() {
                @Override
                public void run() {
                    WatchCallBack watchCallBack = new WatchCallBack();
                    watchCallBack.setZk(zk);

                    String name = Thread.currentThread().getName();
                    watchCallBack.setThreadName(name);
                    //每个线程去抢锁
                    watchCallBack.tryLock();

                    //干活
                    System.out.println(name + " working");
                    try {
                        watchCallBack.getThreadName();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //释放锁
                    watchCallBack.unLock();


                }
            }.start();


        }


        while(true){

        }

    }
}
