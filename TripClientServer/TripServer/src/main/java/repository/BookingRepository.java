package repository;

import domain.Booking;
import myjdbc.MyJdbcTemplate;
import myjdbc.ResultSetExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validator.ValidationException;

import java.util.List;

public class BookingRepository implements CrudRepository<Integer, Booking> {
    private MyJdbcTemplate template;
    private static final Logger LOGGER = LogManager.getLogger(BookingRepository.class.getName());

    public BookingRepository(MyJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Booking findOne(Integer id) {
        LOGGER.traceEntry("finding booking with id {}", id);
        if (id == null) {
            LOGGER.warn("findOne failed null id");
            throw new IllegalArgumentException("Cannot find booking: id must be not null");
        }
        List<Booking> list = template.query(
                String.format("SELECT booking_id, account_id, trip_id, clientName, phoneNumber, numTickets FROM Booking WHERE booking_id=%d", id),
                getBookingResultSetExtractor());
        if (list.isEmpty()) {
            LOGGER.traceExit("no booking found with id {}", id);
            return null;
        }
        LOGGER.traceExit(list.get(0));
        return list.get(0);
    }

    @Override
    public List<Booking> findAll() {
        LOGGER.traceEntry("findAll bookings");
        LOGGER.traceExit();
        return template.query("SELECT booking_id, account_id, trip_id, clientName, phoneNumber, numTickets FROM Booking",
                getBookingResultSetExtractor());
    }

    @Override
    public Booking save(Booking booking) {
        LOGGER.traceEntry("saving {}", booking);
        if (booking == null) {
            LOGGER.warn("save failed null booking");
            throw new IllegalArgumentException("Update failed booking must be not null");
        }
        if (booking.getId() == null) {
            LOGGER.info("inserting {}", booking);
            int id = template.insert(
                    String.format("INSERT INTO Booking(account_id, trip_id, clientName, phoneNumber, numTickets) VALUES(%d, %d, '%s','%s',%d)",
                            booking.getAccountId(),
                            booking.getTripId(),
                            booking.getClientName(),
                            booking.getPhoneNumber(),
                            booking.getNumTickets()));
            booking.setId(id);
            LOGGER.traceExit(booking);
            return booking;
        }
        LOGGER.info("updating {}", booking);
        int lines = template.update(
                String.format("UPDATE Booking SET " +
                                "account_id=%d, " +
                                "trip_id=%d," +
                                "clientName='%s'," +
                                "phoneNumber='%s'," +
                                "numTickets=%d" +
                                " WHERE booking_id=%d",
                        booking.getAccountId(),
                        booking.getTripId(),
                        booking.getClientName(),
                        booking.getPhoneNumber(),
                        booking.getNumTickets(),
                        booking.getId()));
        if (lines == 0) {
            LOGGER.warn("updating failed booking not found");
            throw new ValidationException("Update failed booking not found");
        }
        LOGGER.traceExit(booking);
        return booking;
    }

    @Override
    public Booking delete(Integer id) {
        LOGGER.traceEntry("delete booking with id {}", id);
        if (id == null){
            LOGGER.warn("delete failed null id");
            throw new IllegalArgumentException("Delete failed booking id must be not null");
        }
        Booking booking = findOne(id);
        if (booking == null){
            LOGGER.warn("delete failed booking with id {} not found", id);
            throw new ValidationException("Delete failed booking not found");
        }
        int lines = template.update(String.format("DELETE FROM Booking WHERE booking_id=%d", id));
        if (lines == 0){
            LOGGER.warn("delete failed for booking with id {}", id);
            throw new ValidationException("Delete booking failed");
        }
        LOGGER.traceExit(booking);
        return booking;
    }

    @Override
    public int size() {
        LOGGER.traceEntry("size");
        LOGGER.traceExit();
        return template.query("SELECT COUNT(*) FROM Booking", (rs) -> rs.getInt(1)).get(0);
    }


    private ResultSetExtractor<Booking> getBookingResultSetExtractor() {
        return (rs) -> new Booking(
                rs.getInt("booking_id"),
                rs.getInt("account_id"),
                rs.getInt("trip_id"),
                rs.getString("clientName"),
                rs.getString("phoneNumber"),
                rs.getInt("numTickets"));
    }
}
