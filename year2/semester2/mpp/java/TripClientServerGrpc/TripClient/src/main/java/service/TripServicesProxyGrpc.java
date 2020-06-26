package service;

import controller.MainController;
import domain.Account;
import domain.Trip;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.ProtoUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tripsGrpc.Request;
import tripsGrpc.Response;
import tripsGrpc.TripServicesGrpc;
import validator.ValidationException;

import java.util.List;

public class TripServicesProxyGrpc implements TripServices {
    private static final Logger LOGGER = LogManager.getLogger(TripServicesProxyGrpc.class.getName());
    private TripServicesGrpc.TripServicesBlockingStub blockingStub;
    private TripServicesGrpc.TripServicesStub asyncStub;
    private TripObserver observer;

    public TripServicesProxyGrpc(Channel channel) {
        blockingStub = TripServicesGrpc.newBlockingStub(channel);
        asyncStub = TripServicesGrpc.newStub(channel);
    }

    @Override
    public Account getAccountByNamePassword(String name, String password) {
        LOGGER.info("getting account by name {} and password {}", name, password);
        Request request = ProtoUtils.createLoginRequest(name, password);
        Response response;
        try {
            response = blockingStub.getAccountByNamePassword(request);
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {0}", e);
            throw new ServiceException(e);
        }
        if (response.getType() == Response.ResponseType.ERROR) {
            LOGGER.warn("getting account failed: {}", response.getError());
            if (response.getError().equals("Invalid name or password")) {
                throw new ValidationException(response.getError());
            }
            throw new ServiceException(response.getError());
        }
        Account account = ProtoUtils.getAccount(response);
        LOGGER.info("found account {}", account);
        return account;
    }

    @Override
    public domain.Booking addBooking(String client, String phone, int numTickets, Trip trip, Account account) {
        LOGGER.info("add booking client={}, phone={}, num={}, tripId={}, accountId={}", client, phone, numTickets, trip.getId(), account.getId());
        Request request = ProtoUtils.createAddBookingRequest(client, phone, numTickets, trip, account);
        Response response;
        try {
            response = blockingStub.addBooking(request);
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {0}", e);
            throw new ServiceException(e);
        }
        if (response.getType() == Response.ResponseType.ERROR) {
            LOGGER.warn("adding booking failed: {}", response.getError());
            throw new ServiceException(response.getError());
        }
        LOGGER.info("booking saved");
        return null;
    }

    @Override
    public List<Trip> getAllTrips() {
        LOGGER.info("getting all trips");
        Request request = ProtoUtils.createRequest();
        Response response;
        try {
            response = blockingStub.getAllTrips(request);
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {0}", e);
            throw new ServiceException(e);
        }
        if (response.getType() == Response.ResponseType.ERROR) {
            LOGGER.warn("getting trips failed: {}", response.getError());
            throw new ServiceException(response.getError());
        }
        List<Trip> trips = ProtoUtils.getTrips(response);
        LOGGER.info("found all trips");
        return trips;
    }

    @Override
    public List<Trip> getTripsByLandmarkDepartureHour(String landmark, int start, int end) {
        LOGGER.info("getting searched trips");
        Request request = ProtoUtils.createSearchTripsRequest(landmark, start, end);
        Response response;
        try {
            response = blockingStub.getTripsByLandmarkDepartureHour(request);
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {0}", e);
            throw new ServiceException(e);
        }
        if (response.getType() == Response.ResponseType.ERROR) {
            LOGGER.warn("getting searched trips failed: {}", response.getError());
            throw new ServiceException(response.getError());
        }
        List<Trip> trips = ProtoUtils.getTrips(response);
        LOGGER.info("found searched trips");
        return trips;
    }

    @Override
    public void logOut() {
        LOGGER.info("logging out");
        MainController mc = (MainController) observer;
        int id = mc.getAccount().getId();
        Request request = Request.newBuilder().setAccount(tripsGrpc.Account.newBuilder().setId(id).build()).build();
        Response response = null;
        try {
            response = blockingStub.logout(request);
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {0}", e);
            throw new ServiceException(e);
        }
        if (response.getType() == Response.ResponseType.ERROR) {
            LOGGER.warn("logging out failed: {}", response.getError());
            throw new ServiceException(response.getError());
        }
        LOGGER.info("logged out");
        observer = null;
    }

    @Override
    public void addTripObserver(TripObserver observer) {
        LOGGER.info("adding trip observer");
        this.observer = observer;
        MainController mc = (MainController) observer;
        int id = mc.getAccount().getId();
        Request request = Request.newBuilder().setAccount(tripsGrpc.Account.newBuilder().setId(id).build()).build();
        asyncStub.register(request, new StreamObserver<Response>() {
            @Override
            public void onNext(Response value) {
                LOGGER.info("received response " + value);
                tripsGrpc.Booking b = value.getBooking();
                domain.Booking booking = new domain.Booking(b.getId(), b.getAccount().getId(), b.getTrip().getId(), b.getClient(), b.getPhone(), b.getNumTickets());
                observer.bookingInserted(booking);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                LOGGER.info("observer removed");
            }
        });
    }
}
