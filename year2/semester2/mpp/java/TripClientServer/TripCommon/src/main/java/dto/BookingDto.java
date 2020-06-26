package dto;

import domain.Account;
import domain.Trip;

import java.io.Serializable;

public class BookingDto implements Serializable {
    public Integer id;
    public String client;
    public String phone;
    public int numTickets;
    public TripDto trip;
    public AccountDto account;

    public BookingDto(Integer id, String client, String phone, int numTickets, TripDto trip, AccountDto account) {
        this.id = id;
        this.client = client;
        this.phone = phone;
        this.numTickets = numTickets;
        this.trip = trip;
        this.account = account;
    }

    public BookingDto(String client, String phone, int numTickets, TripDto trip, AccountDto account) {
        this.client = client;
        this.phone = phone;
        this.numTickets = numTickets;
        this.trip = trip;
        this.account = account;
    }
}
