package com.hanshunlie.spring.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkDemo {

    public static void main123(String[] args) {

        CountDownLatch count =new CountDownLatch(1);
        try {
            //watch的注册只发生在读类型调用，get，exists
            ZooKeeper zk = new ZooKeeper("1.15.40.211:2181,1.15.40.211:2182,1.15.40.211:2183", 300000, new Watcher() {

                //回调方法
                @Override
                public void process(WatchedEvent event) {
                    String path = event.getPath();
                    Event.EventType type = event.getType();
                    Event.KeeperState state = event.getState();
                    System.out.println("new zk watch :" + event.toString());


                    switch (state) {
                        case Unknown:
                            break;
                        case Disconnected:
                            break;
                        case NoSyncConnected:
                            break;
                        case SyncConnected:
                            count.countDown();
                            System.out.println("SyncConnected");
                            break;
                        case AuthFailed:
                            break;
                        case ConnectedReadOnly:
                            break;
                        case SaslAuthenticated:
                            break;
                        case Expired:
                            break;
                        case Closed:
                            break;
                    }


                    switch (type) {

                        case None:
                            break;
                        case NodeCreated:
                            break;
                        case NodeDeleted:
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
            });


            count.await();
            ZooKeeper.States state = zk.getState();
            switch (state){

                case CONNECTING:
                    System.out.println("ing...");
                    break;
                case ASSOCIATING:
                    break;
                case CONNECTED:
                    System.out.println("ed...");
                    break;
                case CONNECTEDREADONLY:
                    break;
                case CLOSED:
                    break;
                case AUTH_FAILED:
                    break;
                case NOT_CONNECTED:
                    break;
            }


            String pathname = zk.create("/ooxx", "olddata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            Stat stat=new Stat();
            byte[] data = zk.getData("/ooxx", new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("getdata watch : "+event.toString());
                    try {
                        //watch 为true的时候是default watch
                        zk.getData("/ooxx",this, stat);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, stat);

            System.out.println(new String(data));
            Stat stat1 = zk.setData("/ooxx", "newdata".getBytes(), 0);


            zk.setData("/ooxx","newdata01".getBytes(),stat1.getVersion());

            zk.getData("/ooxx", false, new AsyncCallback.DataCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                    System.out.println("-----------async call -----------");
                    System.out.println(ctx.toString());

                }
            },"abc");
            System.out.println("-----------async over -----------");

            Thread.sleep(11111);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
