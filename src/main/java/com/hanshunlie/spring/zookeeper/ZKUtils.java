package com.hanshunlie.spring.zookeeper;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtils {

    private static ZooKeeper zk;

    private static String address = "1.15.40.211:2181,1.15.40.211:2182,1.15.40.211:2183/testlock";

    private static CountDownLatch init = new CountDownLatch(1);

    private static DefaultWatch watch = new DefaultWatch();

    public static ZooKeeper getZk() {

        try {
            zk = new ZooKeeper(address, 1000, watch);
            watch.setCc(init);


            init.await();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return zk;
    }




}
