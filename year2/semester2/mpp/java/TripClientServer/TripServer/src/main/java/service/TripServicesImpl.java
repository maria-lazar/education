package service;

import domain.Account;
import domain.Booking;
import domain.Trip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.AccountRepository;
import repository.BookingRepository;
import repository.CrudRepository;
import repository.TripRepository;
import validator.AccountValidator;
import validator.BookingValidator;
import validator.TripValidator;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TripServicesImpl implements TripServices {
    private static final Logger LOGGER = LogManager.getLogger(TripServicesImpl.class.getName());

    private TripRepository tripRepository;
    private TripValidator tripValidator;
    private BookingRepository bookingRepository;
    private BookingValidator bookingValidator;
    private AccountRepository accountRepository;
    private AccountValidator accountValidator;
    private List<TripObserver> tripObservers = new ArrayList<>();

    public TripServicesImpl(TripRepository tripRepository, BookingRepository bookingRepository, AccountRepository accountRepository) {
        this.tripRepository = tripRepository;
        this.bookingRepository = bookingRepository;
        this.accountRepository = accountRepository;
    }

    public void setTripValidator(TripValidator tripValidator) {
        this.tripValidator = tripValidator;
    }

    public void setBookingValidator(BookingValidator bookingValidator) {
        this.bookingValidator = bookingValidator;
    }

    public void setAccountValidator(AccountValidator accountValidator) {
        this.accountValidator = accountValidator;
    }

    @Override
    public synchronized Account getAccountByNamePassword(String name, String password) {
        LOGGER.info("getting account by name and password");
        return accountRepository.findByNamePassword(name, password);
    }

    @Override
    public synchronized void addBooking(String client, String phone, int numTickets, Trip trip, Account account) {
        LOGGER.info("adding booking");
        if (trip.getAvailablePlaces() < numTickets) {
            throw new ValidationException("Not enough places available");
        }
        Booking b = new Booking(null, account.getId(), trip.getId(), client, phone, numTickets);
        bookingValidator.validate(b);
        Booking savedBooking = bookingRepository.save(b);
        trip.setAvailablePlaces(trip.getAvailablePlaces() - numTickets);
        tripRepository.save(trip);
        LOGGER.info("booking added");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        LOGGER.info("notifying observers");
        executor.execute(() -> {
            tripObservers.forEach(t -> {
                try {
                    t.bookingInserted(savedBooking);
                } catch (Exception e) {
                    LOGGER.warn("Error notifying observer " + e);
                }
            });
        });
        executor.shutdown();
        LOGGER.info("observers notified");
    }


    @Override
    public synchronized List<Trip> getAllTrips() {
        LOGGER.info("getting all trips");
        return tripRepository.findAll();
    }

    @Override
    public synchronized List<Trip> getTripsByLandmarkDepartureHour(String landmark, int start, int end) {
        LOGGER.info("getting trips by landmark and departure hour");
        return tripRepository.findByLandmarkDepartureHour(landmark, start, end);
    }

    @Override
    public void logOut() {

    }


    @Override
    public synchronized void addTripObserver(TripObserver observer) {
        LOGGER.info("adding observer " + observer);
        tripObservers.add(observer);
    }

    @Override
    public synchronized void removeTripObserver(TripObserver observer) {
        LOGGER.info("removing observer " + observer);
        tripObservers.remove(observer);
    }
}
