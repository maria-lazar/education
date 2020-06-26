package service;

import domain.Account;
import domain.Trip;

import java.util.List;

public interface TripServices {
    Account getAccountByNamePassword(String name, String password);

    void addBooking(String client, String phone, int numTickets, Trip trip, Account account);

    List<Trip> getAllTrips();

    List<Trip> getTripsByLandmarkDepartureHour(String landmark, int start, int end);

    void logOut();

    void addTripObserver(TripObserver observer);

    void removeTripObserver(TripObserver observer);
}
