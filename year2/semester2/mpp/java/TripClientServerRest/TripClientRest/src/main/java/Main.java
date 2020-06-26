import domain.Trip;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TripClient tripClient = new TripClient("http://localhost:8080/trips");
        try {
            getAllTrips(tripClient);
            getById(tripClient);
            add(tripClient);
            update(tripClient);
            delete(tripClient);
        } catch (ServiceException se) {
            System.out.println(se.getMessage());
        }
    }

    public static void getAllTrips(TripClient tripClient) {
        System.out.println("Get all: ");
        tripClient.getAll().forEach(System.out::println);
    }

    public static void getById(TripClient tripClient) {
        try {
            System.out.println("Get by id: ");
            System.out.println(tripClient.getById(1));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
//        System.out.println(tripClient.getById(1));
    }

    public static void add(TripClient tripClient) {
        System.out.println("Add: ");
        System.out.println(tripClient.add(new Trip(null, "trip", "company", LocalDateTime.now(), 10, 100)));
        tripClient.getAll().forEach(System.out::println);
    }

    public static void update(TripClient tripClient) {
        try {
            System.out.println("Update: ");
            List<Trip> trips = tripClient.getAll();
//        trips.forEach(System.out::println);
            Trip trip = trips.get(trips.size() - 1);
            trip.setLandmark("landmark");
            System.out.println(tripClient.update(trip));
            tripClient.getAll().forEach(System.out::println);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delete(TripClient tripClient) {
        try {
            System.out.println("Delete: ");
            List<Trip> trips = tripClient.getAll();
//            trips.forEach(System.out::println);
            Trip trip = trips.get(trips.size() - 1);
            tripClient.delete(trip.getId());
            tripClient.getAll().forEach(System.out::println);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }
}