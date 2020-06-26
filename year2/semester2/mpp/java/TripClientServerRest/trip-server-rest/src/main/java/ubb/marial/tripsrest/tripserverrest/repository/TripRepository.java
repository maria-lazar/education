package ubb.marial.tripsrest.tripserverrest.repository;

import domain.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ubb.marial.tripsrest.tripserverrest.myjdbc.MyJdbcTemplate;
import ubb.marial.tripsrest.tripserverrest.myjdbc.ResultSetExtractor;
import validator.ValidationException;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TripRepository implements CrudRepository<Integer, Trip> {
//    private static final Logger LOGGER = LogManager.getLogger(TripRepository.class.getName());

    private MyJdbcTemplate jdbcTemplate;

    @Autowired
    public TripRepository(MyJdbcTemplate template) {
//        LOGGER.traceEntry("initializing TripRepository");
        this.jdbcTemplate = template;
//        LOGGER.traceExit();
    }
    public TripRepository(){

    }

    public void setJdbcTemplate(MyJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Trip> findByLandmarkDepartureHour(String name, int start, int end){
        List<Trip> trips = jdbcTemplate.query(
                String.format("SELECT trip_id, landmark, companyName, departureTime, price, availablePlaces FROM Trip WHERE landmark='%s'" +
                        " AND (HOUR(departureTime) >= %d AND HOUR(departureTime) < %d)", name, start, end), getTripResultSetExtractor());
        if (trips.isEmpty()){
//            LOGGER.info("no trips found with name {} between {} and {}", name, start, end);
        }
//        LOGGER.traceExit();
        return trips;
    }

    @Override
    public Trip findOne(Integer id) {
//        LOGGER.traceEntry("finding trip with id {}", id);
        if (id == null) {
//            LOGGER.warn("findOne failed null id");
            throw new IllegalArgumentException("Cannot find trip: id must be not null");
        }
        List<Trip> list = jdbcTemplate.query(
                String.format("SELECT trip_id, landmark, companyName, departureTime, price, availablePlaces FROM Trip WHERE trip_id=%d", id),
                getTripResultSetExtractor());
        if (list.isEmpty()) {
//            LOGGER.traceExit("no trip found with id {}", id);
            return null;
        }
//        LOGGER.traceExit(list.get(0));
        return list.get(0);
    }

    @Override
    public List<Trip> findAll() {
//        LOGGER.traceEntry("findAll trips");
//        LOGGER.traceExit();
        return jdbcTemplate.query(
                "SELECT trip_id, landmark, companyName, departureTime, price, availablePlaces FROM Trip",
                getTripResultSetExtractor());
    }

    private ResultSetExtractor<Trip> getTripResultSetExtractor() {
        return (rs) -> new Trip(
                rs.getInt("trip_id"),
                rs.getString("landmark"),
                rs.getString("companyName"),
                rs.getTimestamp("departureTime").toLocalDateTime().truncatedTo(ChronoUnit.MINUTES),
                rs.getFloat("price"),
                rs.getInt("availablePlaces"));
    }

    @Override
    public Trip save(Trip trip) {
//        LOGGER.traceEntry("saving {}", trip);
        if (trip == null) {
//            LOGGER.warn("save failed null trip");
            throw new IllegalArgumentException("Save failed trip must be not null");
        }
        if (trip.getId() == null) {
//            LOGGER.info("inserting {}", trip);
            int id = jdbcTemplate.insert(
                    String.format("INSERT INTO Trip(landmark, companyName, departureTime, price, availablePlaces) VALUES " +
                                    "('%s', '%s', '%s', %f, %d)",
                            trip.getLandmark(),
                            trip.getCompanyName(),
                            String.valueOf(Timestamp.valueOf(trip.getDepartureTime())),
                            trip.getPrice(),
                            trip.getAvailablePlaces()
                    ));
            trip.setId(id);
//            LOGGER.traceExit(trip);
            return trip;
        }
//        LOGGER.info("updating " + trip);
        int lines = jdbcTemplate.update(
                String.format("UPDATE Trip SET landmark='%s', companyName='%s', departureTime='%s', price=%f, availablePlaces=%d WHERE trip_id=%d",
                        trip.getLandmark(),
                        trip.getCompanyName(),
                        String.valueOf(Timestamp.valueOf(trip.getDepartureTime())),
                        trip.getPrice(),
                        trip.getAvailablePlaces(),
                        trip.getId()));
        if (lines == 0) {
//            LOGGER.warn("updating failed trip not found");
            throw new ValidationException("Update failed trip not found");
        }
//        LOGGER.traceExit(trip);
        return trip;
    }


    @Override
    public int size() {
//        LOGGER.traceEntry("size");
        int size = jdbcTemplate.query(
                "SELECT COUNT(*) FROM Trip",
                (rs) -> rs.getInt(1)).get(0);
//        LOGGER.traceExit(size);
        return size;
    }

    @Override
    public Trip delete(Integer id) {
//        LOGGER.traceEntry("delete trip with id {}", id);
        if (id == null){
//            LOGGER.warn("delete failed null id");
            throw new IllegalArgumentException("Delete failed trip id must be not null");
        }
        Trip t = findOne(id);
        if (t == null) {
//            LOGGER.warn("delete failed trip with id {} not found", id);
            throw new ValidationException("Delete failed trip not found");
        }
        int lines = jdbcTemplate.update(String.format("DELETE FROM Trip WHERE trip_id=%d", id));
        if (lines == 0) {
//            LOGGER.warn("delete failed for trip with id {}", id);
            throw new ValidationException("Delete trip failed");
        }
//        LOGGER.traceExit(t);
        return t;
    }
}
