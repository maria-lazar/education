package net;

import domain.Account;
import domain.Trip;
import tripsGrpc.Booking;
import tripsGrpc.Request;
import tripsGrpc.Response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ProtoUtils {
    public static Request createLoginRequest(String name, String password) {
        tripsGrpc.Account account = tripsGrpc.Account.newBuilder().setName(name).setPassword(password).build();
        return Request.newBuilder().setAccount(account).build();
    }

    public static Request createRequest() {
        return Request.newBuilder().build();
    }

    public static Request createSearchTripsRequest(String landmark, int start, int end) {
        tripsGrpc.Trip tripDto = tripsGrpc.Trip.newBuilder().setLandmark(landmark).setStart(start).setEnd(end).build();
        return Request.newBuilder().setTrip(tripDto).build();
    }

    public static Request createAddBookingRequest(String client, String phone, int numTickets, Trip trip, Account account) {
        LocalDateTime dep = trip.getDepartureTime();
        Instant instant = dep.atZone(ZoneId.of("UTC")).toInstant();
        double timeInMillis = instant.toEpochMilli();
        tripsGrpc.Trip t = tripsGrpc.Trip.newBuilder().setId(trip.getId()).setLandmark(trip.getLandmark())
                .setAvailablePlaces(trip.getAvailablePlaces()).setCompanyName(trip.getCompanyName())
                .setPrice(trip.getPrice()).setDepartureTime(timeInMillis).build();
        tripsGrpc.Account a = tripsGrpc.Account.newBuilder().setId(account.getId()).setName(account.getName())
                .setPassword(account.getPassword()).build();
        Booking booking = Booking.newBuilder().setClient(client).setAccount(a).setNumTickets(numTickets)
                .setPhone(phone).setTrip(t).build();
        return Request.newBuilder().setBooking(booking).build();
    }


    public static Account getAccount(Response response) {
        tripsGrpc.Account account = response.getAccount();
        return new Account(account.getId(), account.getName(), account.getPassword());
    }


    public static List<Trip> getTrips(Response response) {
        List<tripsGrpc.Trip> trips = response.getTripsList();
        return trips.stream()
                .map(ProtoUtils::convertToTrip)
                .collect(Collectors.toList());
    }

    public static Trip convertToTrip(tripsGrpc.Trip t) {
        LocalDateTime departure = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) t.getDepartureTime()), ZoneId.of("UTC"));
        return new Trip(t.getId(), t.getLandmark(), t.getCompanyName(), departure, t.getPrice(), t.getAvailablePlaces());
    }

    public static Account convertToAccount(tripsGrpc.Account account) {
        return new Account(account.getId(), account.getName(), account.getPassword());
    }
}
