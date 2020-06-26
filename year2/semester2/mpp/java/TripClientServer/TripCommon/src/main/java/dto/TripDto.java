package dto;

import jdk.internal.net.http.common.Pair;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TripDto implements Serializable {
    public Integer id;
    public String landmark;
    public String companyName;
    public LocalDateTime departureTime;
    public float price;
    public int availablePlaces;
    public ArrayList<Integer> interval;

    public TripDto(String landmark, ArrayList<Integer> interval) {
        this.landmark = landmark;
        this.interval = interval;
    }

    public TripDto(Integer id, String landmark, String companyName, LocalDateTime departureTime, float price, int availablePlaces) {
        this.id = id;
        this.landmark = landmark;
        this.companyName = companyName;
        this.departureTime = departureTime;
        this.price = price;
        this.availablePlaces = availablePlaces;
    }

    public TripDto(Integer tripId) {
        id = tripId;
    }

    @Override
    public String toString() {
        return "TripDto{" +
                "id=" + id +
                ", landmark='" + landmark + '\'' +
                ", companyName='" + companyName + '\'' +
                ", departureTime=" + departureTime +
                ", price=" + price +
                ", availablePlaces=" + availablePlaces +
                '}';
    }
}
