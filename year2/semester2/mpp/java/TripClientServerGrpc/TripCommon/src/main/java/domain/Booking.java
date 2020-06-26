package domain;

import java.util.Objects;

public class Booking extends Entity<Integer> {
    private Integer accountId;
    private Integer tripId;
    private String clientName;
    private String phoneNumber;
    private int numTickets;

    public Booking() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return numTickets == booking.numTickets &&
                accountId.equals(booking.accountId) &&
                tripId.equals(booking.tripId) &&
                clientName.equals(booking.clientName) &&
                phoneNumber.equals(booking.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, tripId, clientName, phoneNumber, numTickets);
    }

    public Booking(Integer id, Integer accountId, Integer tripId, String clientName, String phoneNumber, int numTickets) {
       this.id = id;
        this.accountId = accountId;
        this.tripId = tripId;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.numTickets = numTickets;
    }

    @Override
    public String toString() {
        return id + " " + accountId + " " + tripId + " " + clientName + " " + phoneNumber + " " + numTickets;
    }


    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

}
