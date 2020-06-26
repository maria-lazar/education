import domain.Trip;
import myjdbc.MyJdbcTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.TripRepository;
import validator.ValidationException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TripRepositoryTest {

    private TripRepository tripRepository;
    private MyJdbcTemplate template;

    @Before
    public void setUp() throws Exception {
        template = new MyJdbcTemplate("db.properties");
        tripRepository = new TripRepository(template);
    }

    @After
    public void tearDown() throws Exception {
        template.closeConnection();
    }

    @Test
    public void findOne() {
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), 10, 10));
            int id = t.getId();
            assertEquals(t, tripRepository.findOne(id));
            tripRepository.delete(id);
            assertEquals(null, tripRepository.findOne(id));
            try {
                tripRepository.findOne(null);
                fail();
            } catch (IllegalArgumentException ae) {

            }
            t = null;
        } catch (Exception e) {

        }finally {
            if (t != null){
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void findAll() {
        try {
            int size = tripRepository.size();
            assertEquals(size, tripRepository.findAll().size());
        } catch (Exception e) {

        }
    }

    @Test
    public void insertInvalid() {
        try {
            tripRepository.save(null);
            fail();
        } catch (IllegalArgumentException ie) {

        }
    }

    @Test
    public void updateValid() {
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now(), 10, 10));
            int id = t.getId();
            int size = tripRepository.size();
            t = tripRepository.save(new Trip(id, "z", "a", LocalDateTime.now(), 10, 10));
            assertEquals("z", t.getLandmark());
            assertEquals(size, tripRepository.size());
        } catch (Exception e) {

        } finally {
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void updateInvalid() {
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now(), 10, 10));
            int id = t.getId();
            tripRepository.delete(id);
            try {
                t = tripRepository.save(new Trip(id, "z", "a", LocalDateTime.now(), 10, 10));
                fail();
            } catch (ValidationException ve) {

            }
        } catch (Exception e) {

        }
    }

    @Test
    public void insertValid() {
        Trip t = null;
        try {
            int size = tripRepository.size();
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now(), 10, 10));
            assertEquals(size + 1, tripRepository.size());
        } catch (Exception e) {
        } finally {
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
        }
    }

    @Test
    public void deleteValid() {
        Trip t = null;
        try {
            int size = tripRepository.size();
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now(), 10, 10));
            assertEquals(size + 1, tripRepository.size());
            t = tripRepository.delete(t.getId());
            assertEquals(size, tripRepository.size());
            t = null;
        } catch (Exception e) {
        } finally {
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
        }
    }

    @Test
    public void deleteInvalid() {
        Trip t = null;
        try {
            int size = tripRepository.size();
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now(), 10, 10));
            assertEquals(size + 1, tripRepository.size());
            t = tripRepository.delete(t.getId());
            assertEquals(size, tripRepository.size());
            try {
                tripRepository.delete(t.getId());
                fail();
            } catch (ValidationException ve) {

            } catch (IllegalArgumentException ie) {

            }
            try {
                tripRepository.delete(null);
                fail();
            } catch (IllegalArgumentException ie) {

            }
            t = null;
        } catch (Exception e) {
        } finally {
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
        }
    }
}