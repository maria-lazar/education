package service;

import domain.Account;
import domain.Booking;
import domain.Trip;

import java.util.List;

public interface TripServices {
    Account getAccountByNamePassword(String name, String password);

    Booking addBooking(String client, String phone, int numTickets, Trip trip, Account account);

    List<Trip> getAllTrips();

    List<Trip> getTripsByLandmarkDepartureHour(String landmark, int start, int end);

    void logOut();

    void addTripObserver(TripObserver observer);
}
