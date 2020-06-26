package com.spring.bugs.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BugsObserver extends Remote {
    void updateEmployeeList() throws RemoteException;

    void updateBugsList() throws RemoteException;
}
