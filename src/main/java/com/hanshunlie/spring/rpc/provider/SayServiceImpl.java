package com.hanshunlie.spring.rpc.provider;

import java.rmi.RemoteException;

public class SayServiceImpl implements ISayService {

    @Override
    public String say(String name) throws RemoteException {
        System.err.println("say name :" + name);
        return "say name :" + name;
    }
}
