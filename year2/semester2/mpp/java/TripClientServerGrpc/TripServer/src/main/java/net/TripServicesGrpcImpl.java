package net;

import domain.Account;
import domain.Trip;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.TripServices;
import tripsGrpc.Booking;
import tripsGrpc.Request;
import tripsGrpc.Response;
import tripsGrpc.TripServicesGrpc;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TripServicesGrpcImpl extends TripServicesGrpc.TripServicesImplBase {
    private static final Logger LOGGER = LogManager.getLogger(TripServicesGrpcImpl.class.getName());
    private TripServices tripServices;

    public TripServicesGrpcImpl(TripServices tripServices) {
        this.tripServices = tripServices;
    }

    private ConcurrentHashMap<Integer, StreamObserver<Response>> observers = new ConcurrentHashMap<>();

    @Override
    public void getAccountByNamePassword(Request request, StreamObserver<Response> responseObserver) {
        Account a = ProtoUtilsServer.getAccount(request);
        LOGGER.info("getting account by name {} and password {}", a.getName(), a.getPassword());
        Response response = null;
        try {
            Account account = tripServices.getAccountByNamePassword(a.getName(), a.getPassword());
            response = ProtoUtilsServer.createLoginResponse(account);
        } catch (Exception e) {
            response = ProtoUtilsServer.createErrorResponse(e);
        }
        LOGGER.info("sending response {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void register(Request request, StreamObserver<Response> responseObserver) {
        LOGGER.info("adding observer " + responseObserver);
        tripsGrpc.Account account = request.getAccount();
        observers.put(account.getId(), responseObserver);
    }


    @Override
    public void getAllTrips(Request request, StreamObserver<Response> responseObserver) {
        LOGGER.info("getting all trips");
        Response response = null;
        try {
            List<Trip> t = tripServices.getAllTrips();
            response = ProtoUtilsServer.createGetTripsResponse(t);
        } catch (Exception e) {
            response = ProtoUtilsServer.createErrorResponse(e);
        }
        LOGGER.info("sending get all trips response {}", response.getType());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addBooking(Request request, StreamObserver<Response> responseObserver) {
        LOGGER.info("add booking");
        Response response = null;
        try {
            Booking booking = request.getBooking();
            Trip trip = ProtoUtils.convertToTrip(booking.getTrip());
            Account account = ProtoUtils.convertToAccount(booking.getAccount());
            domain.Booking savedBooking = tripServices.addBooking(booking.getClient(), booking.getPhone(), booking.getNumTickets(), trip, account);
            response = ProtoUtilsServer.createOkResponse();

            LOGGER.info("notifying observers");
            observers.values().forEach(o -> {
                LOGGER.info("notify " + o);
                Booking bookingDto = Booking.newBuilder().setId(savedBooking.getId())
                        .setClient(savedBooking.getClientName()).setAccount(booking.getAccount()).setTrip(booking.getTrip())
                        .setPhone(booking.getPhone()).setNumTickets(booking.getNumTickets()).build();
                Response response1 = Response.newBuilder().setBooking(bookingDto).build();
                o.onNext(response1);
            });
        } catch (Exception e) {
            response = Response.newBuilder().setType(Response.ResponseType.ERROR).setError(e.getMessage()).build();
        }
        LOGGER.info("add booking response {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTripsByLandmarkDepartureHour(Request request, StreamObserver<Response> responseObserver) {
        LOGGER.info("searching trips");
        Response response = null;
        try {
            tripsGrpc.Trip t = request.getTrip();
            List<Trip> tripList = tripServices.getTripsByLandmarkDepartureHour(t.getLandmark(), t.getStart(), t.getEnd());
            response = ProtoUtilsServer.createGetTripsResponse(tripList);
        } catch (Exception e) {
            response = Response.newBuilder().setType(Response.ResponseType.ERROR).setError(e.getMessage()).build();
        }
        LOGGER.info("sending search trips response {}", response.getType());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void logout(Request request, StreamObserver<Response> responseObserver) {
        tripsGrpc.Account account = request.getAccount();
        observers.remove(account.getId());
        LOGGER.info("removing observer {}",  account.getId());
        responseObserver.onNext(ProtoUtilsServer.createOkResponse());
        responseObserver.onCompleted();
    }
}
//    @Override
//    public void register(StreamObserver<Response> responseObserver) {
//        LOGGER.info("adding observer " + responseObserver);
//        observers.add(responseObserver);
//        return new StreamObserver<Request>() {
//            @Override
//            public void onNext(Request value) {
//
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onCompleted() {
//                LOGGER.info("removing observer " + responseObserver);
//                responseObserver.onCompleted();
//                observers.remove(responseObserver);
//            }
//        };
//    }

