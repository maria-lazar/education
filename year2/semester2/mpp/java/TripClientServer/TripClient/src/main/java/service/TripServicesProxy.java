package service;

import domain.Account;
import domain.Booking;
import domain.Trip;
import dto.AccountDto;
import dto.BookingDto;
import dto.TripDto;
import net.Request;
import net.RequestType;
import net.Response;
import net.ResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TripServicesProxy extends BaseServiceProxy implements TripServices {
    private static final Logger LOGGER = LogManager.getLogger(TripServicesProxy.class.getName());
    protected TripObserver tripObserver;

    public TripServicesProxy(String host, int port) {
        super(host, port);
    }

    @Override
    public Account getAccountByNamePassword(String name, String password) {
        LOGGER.traceEntry("getting account by name " + name + " and password " + password);
        ensureConnected();
        Request req = new Request.Builder()
                .type(RequestType.LOGIN)
                .data(new AccountDto(name, password))
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            LOGGER.info("received OK response");
            AccountDto accountDto = (AccountDto) response.data();
            if (accountDto == null) {
                LOGGER.info("no account found");
                return null;
            }
            Account a = new Account(accountDto.id, accountDto.name, accountDto.password);
            LOGGER.info("found account " + a);
            return a;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            LOGGER.info("received ERROR response " + err);
            throw new ServiceException(err);
        }
        LOGGER.traceExit();
        return null;
    }

    @Override
    public void addBooking(String client, String phone, int numTickets, Trip trip, Account account) {
        LOGGER.traceEntry("adding booking for client " + client + " to trip " + trip + " made by " + account);
        ensureConnected();
        TripDto tripDto = new TripDto(trip.getId(), trip.getLandmark(), trip.getCompanyName(), trip.getDepartureTime(),
                                      trip.getPrice(), trip.getAvailablePlaces());
        AccountDto accountDto = new AccountDto(account.getId(), account.getName(), account.getPassword());
        Request req = new Request.Builder()
                .type(RequestType.ADD_BOOKING)
                .data(new BookingDto(client, phone, numTickets, tripDto, accountDto))
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            LOGGER.info("received OK response");
            LOGGER.info("booking added");
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            LOGGER.info("add booking failed received ERROR response " + err);
//            closeConnection();
            throw new ServiceException(err);
        }
        LOGGER.traceExit();
    }

    @Override
    public List<Trip> getAllTrips() {
        LOGGER.info("getting all trips");
        ensureConnected();
        Request req = new Request.Builder()
                .type(RequestType.GET_ALL_TRIPS)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            LOGGER.info("getting all trips OK response");
            List<TripDto> trips = (ArrayList<TripDto>) response.data();
            return trips.stream()
                    .map(t -> new Trip(t.id, t.landmark, t.companyName, t.departureTime, t.price, t.availablePlaces))
                    .collect(Collectors.toList());
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            LOGGER.warn("getting all trips failed");
//            closeConnection();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public List<Trip> getTripsByLandmarkDepartureHour(String landmark, int start, int end) {
        LOGGER.traceEntry("getting trips with landmark " + landmark + " and between " + start + " " + end);
        ensureConnected();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(start);
        list.add(end);
        Request req = new Request.Builder()
                .type(RequestType.SEARCH_TRIPS)
                .data(new TripDto(landmark, list))
                .build();
        sendRequest(req);
        Response response = readResponse();
        LOGGER.info("response for search trips " + response);
        if (response.type() == ResponseType.OK) {
            LOGGER.info("received OK response");
            List<TripDto> trips = (ArrayList<TripDto>) response.data();
            LOGGER.info("returning searched trips ");
            return trips.stream()
                    .map(t -> new Trip(t.id, t.landmark, t.companyName, t.departureTime, t.price, t.availablePlaces))
                    .collect(Collectors.toList());
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            LOGGER.info("searching trips failed received ERROR response " + err);
            throw new ServiceException(err);
        }
        LOGGER.traceExit();
        return null;
    }

    @Override
    public void logOut() {
        LOGGER.info("logging out");
//        ensureConnected();
        if (connection == null){
            LOGGER.info("connection already closed");
            return;
        }
        Request req = new Request.Builder().type(RequestType.LOGOUT).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            LOGGER.warn("logging out failed " + err);
            throw new ServiceException(err);
        }
    }

    protected void handleBookingAdded(Response response) {
        BookingDto bookingDto = (BookingDto)response.data();
        Booking b = new Booking(bookingDto.id, bookingDto.account.id, bookingDto.trip.id, bookingDto.client, bookingDto.phone, bookingDto.numTickets);
        tripObserver.bookingInserted(b);
    }
    @Override
    public void addTripObserver(TripObserver observer) {
        tripObserver = observer;
    }

    @Override
    public void removeTripObserver(TripObserver observer) {
        tripObserver = null;
    }
}
