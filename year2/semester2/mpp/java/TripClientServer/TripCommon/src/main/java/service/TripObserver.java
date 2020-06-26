package service;

import domain.Booking;

public interface TripObserver {
    void bookingInserted(Booking booking);
}
