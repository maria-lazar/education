import domain.Trip;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class TripClient {
    private RestTemplate restTemplate = new RestTemplate();
    private String url;

    public TripClient(String url) {
        this.url = url;
    }

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception he) {
            throw new ServiceException(he);
        }
    }

    public List<Trip> getAll() {
        Trip[] trips = execute(() -> restTemplate.getForObject(url, Trip[].class));
        return new ArrayList<Trip>(Arrays.asList(trips));
    }

    public Trip getById(int id) {
        String urlGetById = String.format("%s/%d", url, id);
        ResponseEntity<Trip> e = execute(() -> restTemplate.getForEntity(urlGetById, Trip.class));
        return e.getBody();
    }

    public Trip add(Trip trip) {
        return execute(() -> restTemplate.postForObject(url, trip, Trip.class));
    }

    public Trip update(Trip trip) {
        String urlPut = String.format("%s/%d", url, trip.getId());
        execute(() -> {
            restTemplate.put(urlPut, trip, Trip.class);
            return null;
        });
        return trip;
    }

    public void delete(int i) {
        String urlPut = String.format("%s/%d", url, i);
        execute(() -> {
            restTemplate.delete(urlPut);
            return null;
        });
    }
}
