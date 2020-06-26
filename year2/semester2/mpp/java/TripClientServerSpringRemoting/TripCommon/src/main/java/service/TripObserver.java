package service;

import domain.Booking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TripObserver extends Remote {
    void bookingInserted(Booking booking) throws RemoteException;
}
