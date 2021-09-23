package com.hanshunlie.spring.rpc.consumer;

import com.hanshunlie.spring.rpc.provider.ISayService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConsumerMain {
    public static void main(String[] args) throws Exception{
        //注册中心
        Registry registry = LocateRegistry.getRegistry(999);

        //发现服务
        ISayService sayService = (ISayService) registry.lookup("sayService");

        //调起服务
        String say = sayService.say("234567890-");
        System.out.println(say);
    }
}
