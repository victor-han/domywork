package com.hanshunlie.spring.rpc.provider;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISayService extends Remote {

    String say(String name) throws RemoteException;
}
