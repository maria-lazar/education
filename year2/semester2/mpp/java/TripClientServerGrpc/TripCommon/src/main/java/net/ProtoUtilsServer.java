package net;

import domain.Account;
import domain.Trip;
import tripsGrpc.Request;
import tripsGrpc.Response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ProtoUtilsServer {
    public static Response createLoginResponse(Account account) {
        Response response = null;
        if (account == null) {
            response = Response.newBuilder().setType(Response.ResponseType.ERROR).setError("Invalid name or password").build();
        } else {
            tripsGrpc.Account a = tripsGrpc.Account.newBuilder().setId(account.getId()).setName(account.getName()).setPassword(account.getPassword()).build();
            response = Response.newBuilder().setType(Response.ResponseType.OK).setAccount(a).build();
        }
        return response;
    }

    public static Response createErrorResponse(Exception e) {
        return Response.newBuilder().setType(Response.ResponseType.ERROR).setError(e.getMessage()).build();
    }

    public static Response createGetTripsResponse(List<Trip> t) {
        Response.Builder builder = Response.newBuilder().setType(Response.ResponseType.OK);
        t.forEach(el -> {
            LocalDateTime departure = el.getDepartureTime();
            Instant instant = departure.atZone(ZoneId.of("UTC")).toInstant();
            double timeInMillis = instant.toEpochMilli();

            tripsGrpc.Trip trip = tripsGrpc.Trip.newBuilder().setId(el.getId()).setLandmark(el.getLandmark())
                    .setCompanyName(el.getCompanyName()).setDepartureTime(timeInMillis).setPrice(el.getPrice()).setAvailablePlaces(el.getAvailablePlaces()).build();
            builder.addTrips(trip);
        });
        return builder.build();
    }

    public static Response createOkResponse() {
        return Response.newBuilder().setType(Response.ResponseType.OK).build();
    }

    public static Account getAccount(Request request) {
        tripsGrpc.Account account = request.getAccount();
        return new Account(account.getId(), account.getName(), account.getPassword());
    }
}
