package net;

import domain.Account;
import domain.Booking;
import domain.Trip;
import dto.AccountDto;
import dto.BookingDto;
import dto.TripDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.TripObserver;
import service.TripServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;


public class TripClientRpcWorker implements Runnable, TripObserver {
    private TripServices service;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static final Logger LOGGER = LogManager.getLogger(TripClientRpcWorker.class.getName());

    public TripClientRpcWorker(TripServices tripServices, Socket connection) {
        LOGGER.traceEntry("initializing worker");
        this.service = tripServices;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            LOGGER.warn("initializing worker failed");
            e.printStackTrace();
        }
    }

    public void run() {
        LOGGER.traceEntry("running worker");
        while (connected) {
            try {
                Object request = input.readObject();
                LOGGER.info("received request " + request);
                Response response = handleRequest((Request) request);
                LOGGER.info("response for request " + response);
                if (response != null) {
                    LOGGER.info("sending response " + response);
                    sendResponse(response);
                }
            } catch (Exception e) {
                if (connected) {
                    LOGGER.warn("reading request failed");
                    e.printStackTrace();
                }
                break;
            }
        }
        try {
            LOGGER.info("closing connection");
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            LOGGER.warn("closing connection failed");
            e.printStackTrace();
        }
    }


    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private static Response errorResponse = new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request) {
        LOGGER.info("handling request " + request);
        if (request.type() == RequestType.LOGIN) {
            LOGGER.info("handling LOGIN request");
            AccountDto accountDto = (AccountDto) request.data();
            try {
                LOGGER.info("finding account with name " + accountDto.name + "password " + accountDto.password);
                Account a = service.getAccountByNamePassword(accountDto.name, accountDto.password);
                LOGGER.info("found account " + a);
                if (a == null) {
                    return okResponse;
                }
                service.addTripObserver(this);
                return new Response.Builder()
                        .type(ResponseType.OK)
                        .data(new AccountDto(a.getId(), a.getName(), a.getPassword()))
                        .build();
            } catch (Exception e) {
                LOGGER.warn("finding account failed");
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.GET_ALL_TRIPS) {
            LOGGER.info("handling GET_ALL_TRIPS request");
            try {
                List<Trip> trips = service.getAllTrips();
                List<TripDto> tripsDto = trips.stream()
                        .map(t -> new TripDto(t.getId(), t.getLandmark(), t.getCompanyName(), t.getDepartureTime(), t.getPrice(), t.getAvailablePlaces()))
                        .collect(Collectors.toList());
                LOGGER.info("returning response with trips");
                return new Response.Builder()
                        .type(ResponseType.OK)
                        .data(tripsDto)
                        .build();
            } catch (Exception e) {
                LOGGER.warn("getting all trips failed");
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            LOGGER.info("Logout request");
            //service.logOut();
            service.removeTripObserver(this);
            connected = false;
            return okResponse;
        }
        if (request.type() == RequestType.SEARCH_TRIPS) {
            try {
                LOGGER.info("handling SEARCH_TRIPS request");
                TripDto tripDto = (TripDto) request.data();
                List<Trip> trips = service.getTripsByLandmarkDepartureHour(tripDto.landmark, tripDto.interval.get(0), tripDto.interval.get(1));
                List<TripDto> tripsDto = trips.stream()
                        .map(t -> new TripDto(t.getId(), t.getLandmark(), t.getCompanyName(), t.getDepartureTime(), t.getPrice(), t.getAvailablePlaces()))
                        .collect(Collectors.toList());
                LOGGER.info("returning response with trips searched");
                return new Response.Builder()
                        .type(ResponseType.OK)
                        .data(tripsDto)
                        .build();
            } catch (Exception e) {
                LOGGER.warn("getting searched trips failed");
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.ADD_BOOKING) {
            LOGGER.info("handling ADD_BOOKING request");
            try {
                BookingDto bookingDto = (BookingDto) request.data();
                TripDto t = bookingDto.trip;
                AccountDto a = bookingDto.account;
                service.addBooking(bookingDto.client, bookingDto.phone, bookingDto.numTickets,
                        new Trip(t.id, t.landmark, t.companyName, t.departureTime, t.price, t.availablePlaces),
                        new Account(a.id, a.name, a.password));
                LOGGER.info("saved booking returning ok response");
                return okResponse;
            } catch (Exception ve) {
                LOGGER.warn("saving booking failed");
                return new Response.Builder().type(ResponseType.ERROR).data(ve.getMessage()).build();
            }
        }
        LOGGER.traceExit();
        return null;
    }

    private void sendResponse(Response response) throws IOException {
        LOGGER.info("sending response to client " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void bookingInserted(Booking booking) {
        LOGGER.info("sending booking inserted response");
        BookingDto bookingDto = new BookingDto(booking.getId(), booking.getClientName(), booking.getPhoneNumber(),
                booking.getNumTickets(), new TripDto(booking.getTripId()), new AccountDto(booking.getAccountId()));
        Response response = new Response.Builder().type(ResponseType.BOOKING_ADDED).data(bookingDto).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            LOGGER.info("sending response booking inserted failed");
            e.printStackTrace();
        }
    }
}
