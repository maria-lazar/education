package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Trip implements Entity<Integer>{
    private String landmark;
    private String companyName;
    private LocalDateTime departureTime;
    private float price;
    private int availablePlaces;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Trip() {

    }

    @Override
    public String toString() {
        return  id + " " + landmark + " " + companyName + " " + departureTime + " " + price + " " + availablePlaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Float.compare(trip.price, price) == 0 &&
                availablePlaces == trip.availablePlaces &&
                landmark.equals(trip.landmark) &&
                companyName.equals(trip.companyName) &&
                departureTime.equals(trip.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(landmark, companyName, departureTime, price, availablePlaces);
    }

    public Trip(Integer id, String landmark, String companyName, LocalDateTime departureTime, float price, int availablePlaces) {
        this.id = id;
        this.landmark = landmark;
        this.companyName = companyName;
        this.departureTime = departureTime;
        this.price = price;
        this.availablePlaces = availablePlaces;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(int availablePlaces) {
        this.availablePlaces = availablePlaces;
    }
}
