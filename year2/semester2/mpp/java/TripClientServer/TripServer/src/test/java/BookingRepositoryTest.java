import domain.Account;
import domain.Booking;
import domain.Trip;
import myjdbc.MyJdbcTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.AccountRepository;
import repository.BookingRepository;
import repository.TripRepository;
import validator.ValidationException;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class BookingRepositoryTest {
    private BookingRepository repository;
    private TripRepository tripRepository;
    private AccountRepository accountRepository;
    private MyJdbcTemplate template;

    @Before
    public void setUp() throws Exception {
        template = new MyJdbcTemplate("db.properties");
        repository = new BookingRepository(template);
        tripRepository = new TripRepository(template);
        accountRepository = new AccountRepository(template);
    }

    @After
    public void tearDown() throws Exception {
        template.closeConnection();
    }

    @Test
    public void findOne() {
        Booking b = null;
        Trip t = null;
        Account a = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now().withNano(0), 10, 10));
            a = accountRepository.save(new Account(null, "a", "b"));
            b = repository.save(new Booking(null, a.getId(), t.getId(), "a", "b", 10));
            int id = b.getId();
            assertEquals(b, repository.findOne(id));
            repository.delete(id);
            assertEquals(null, repository.findOne(id));
            try {
                repository.findOne(null);
                fail();
            } catch (IllegalArgumentException ae) {

            }
        } catch (Exception e) {

        } finally {
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {

                }
            }
            if (a != null) {
                try {
                    accountRepository.delete(a.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void findAll() {
        try {
            int size = repository.size();
            assertEquals(size, repository.findAll().size());
        } catch (Exception e) {

        }
    }

    @Test
    public void insertInvalid() {
        try {
            repository.save(null);
            fail();
        } catch (IllegalArgumentException ie) {

        }
    }

    @Test
    public void insertValid() {
        Booking b = null;
        Account a = null;
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now().withNano(0), 10, 10));
            a = accountRepository.save(new Account(null, "a", "b"));
            int size = repository.size();
            b = repository.save(new Booking(null, a.getId(), t.getId(), "a", "b", 10));
            assertEquals(size + 1, repository.size());
        } catch (Exception e) {
        } finally {
            if (b != null) {
                try {
                    repository.delete(b.getId());
                } catch (Exception e) {

                }
            }
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
            if (a != null) {
                try {
                    accountRepository.delete(a.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void updateValid() {
        Booking b = null;
        Account a = null;
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now().withNano(0), 10, 10));
            a = accountRepository.save(new Account(null, "a", "b"));
            b = repository.save(new Booking(null, a.getId(), t.getId(), "a", "b", 10));
            int id = b.getId();
            int size = repository.size();
            b = repository.save(new Booking(id, a.getId(), t.getId(), "z", "b", 10));
            assertEquals(size, repository.size());
            assertEquals("z", b.getClientName());
        } catch (Exception e) {
        } finally {
            if (b != null) {
                try {
                    repository.delete(b.getId());
                } catch (Exception e) {

                }
            }
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
            if (a != null) {
                try {
                    accountRepository.delete(a.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void updateInvalid() {
        Booking b = null;
        Account a = null;
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now().withNano(0), 10, 10));
            a = accountRepository.save(new Account(null, "a", "b"));
            b = repository.save(new Booking(null, a.getId(), t.getId(), "a", "b", 10));
            int id = b.getId();
            repository.delete(b.getId());
            try {
                b = repository.save(new Booking(id, a.getId(), t.getId(), "z", "b", 10));
                fail();
            } catch (ValidationException ve) {

            }
            b = null;
        } catch (Exception e) {
        } finally {
            if (b != null) {
                try {
                    repository.delete(b.getId());
                } catch (Exception e) {

                }
            }
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
            if (a != null) {
                try {
                    accountRepository.delete(a.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void deleteValid(){
        Booking b = null;
        Account a = null;
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now().withNano(0), 10, 10));
            a = accountRepository.save(new Account(null, "a", "b"));
            b = repository.save(new Booking(null, a.getId(), t.getId(), "a", "b", 10));
            int id = b.getId();
            int size = repository.size();
            repository.delete(b.getId());
            assertEquals(size - 1, repository.size());
            b = null;
        } catch (Exception e) {
        } finally {
            if (b != null) {
                try {
                    repository.delete(b.getId());
                } catch (Exception e) {

                }
            }
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
            if (a != null) {
                try {
                    accountRepository.delete(a.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void deleteInvalid(){
        Booking b = null;
        Account a = null;
        Trip t = null;
        try {
            t = tripRepository.save(new Trip(null, "a", "a", LocalDateTime.now().withNano(0), 10, 10));
            a = accountRepository.save(new Account(null, "a", "b"));
            b = repository.save(new Booking(null, a.getId(), t.getId(), "a", "b", 10));
            int id = b.getId();
            repository.delete(b.getId());
            try{
                repository.delete(b.getId());
                fail();
            }catch(ValidationException ve){

            }
            try{
                repository.delete(null);
                fail();
            }catch (IllegalArgumentException ie){

            }
            b = null;
        } catch (Exception e) {
        } finally {
            if (b != null) {
                try {
                    repository.delete(b.getId());
                } catch (Exception e) {

                }
            }
            if (t != null) {
                try {
                    tripRepository.delete(t.getId());
                } catch (Exception e) {
                }
            }
            if (a != null) {
                try {
                    accountRepository.delete(a.getId());
                } catch (Exception e) {

                }
            }
        }
    }
}