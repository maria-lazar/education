import domain.Account;
import myjdbc.MyJdbcTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.AccountRepository;
import validator.ValidationException;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AccountRepositoryTest {
    private AccountRepository repository;
    private MyJdbcTemplate template;

    @Before
    public void setUp() throws Exception {
        template = new MyJdbcTemplate("db.properties");
        repository = new AccountRepository(template);
    }

    @After
    public void tearDown() throws Exception {
        template.closeConnection();
    }

    @Test
    public void findOne() {
        Account a = null;
        try {
            a = repository.save(new Account(null, "a", "b"));
            int id = a.getId();
            assertEquals(a, repository.findOne(id));
            repository.delete(id);
            assertEquals(null, repository.findOne(id));
            try {
                repository.findOne(null);
                fail();
            } catch (IllegalArgumentException ae) {

            }
        } catch (Exception e) {

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
        Account a = null;
        try {
            int size = repository.size();
            a = repository.save(new Account(null, "a", "b"));
            assertEquals(size + 1, repository.size());
        } catch (Exception e) {
        } finally {
            if (a != null) {
                try {
                    repository.delete(a.getId());
                } catch (Exception e) {
                }
            }
        }
    }

    @Test
    public void updateValid() {
        Account a = null;
        try {
            a = repository.save(new Account(null, "a", "b"));
            int id = a.getId();
            int size = repository.size();
            a = repository.save(new Account(id, "a", "z"));
            assertEquals("z", a.getPassword());
            assertEquals(size, repository.size());
        } catch (Exception e) {

        } finally {
            if (a != null) {
                try {
                    repository.delete(a.getId());
                } catch (Exception e) {

                }
            }
        }
    }

    @Test
    public void updateInvalid() {
        Account a = null;
        try {
            a = repository.save(new Account(null, "a", "b"));
            int id = a.getId();
            repository.delete(id);
            try {
                a = repository.save(new Account(id, "z", "z"));
                fail();
            } catch (ValidationException ve) {

            }
        } catch (Exception e) {

        }
    }

    @Test
    public void deleteValid() {
        Account a = null;
        try {
            int size = repository.size();
            a = repository.save(new Account(null, "a", "b"));
            assertEquals(size + 1, repository.size());
            a = repository.delete(a.getId());
            assertEquals(size, repository.size());
            a = null;
        } catch (Exception e) {
        } finally {
            if (a != null) {
                try {
                    repository.delete(a.getId());
                } catch (Exception e) {
                }
            }
        }
    }

    @Test
    public void deleteInvalid() {
        Account a = null;
        try {
            int size = repository.size();
            a = repository.save(new Account(null, "a", "b"));
            assertEquals(size + 1, repository.size());
            a = repository.delete(a.getId());
            assertEquals(size, repository.size());
            try {
                repository.delete(a.getId());
                fail();
            } catch (ValidationException ve) {

            } catch (IllegalArgumentException ie) {

            }
            try {
                repository.delete(null);
                fail();
            } catch (IllegalArgumentException ie) {

            }
            a = null;
        } catch (Exception e) {
        } finally {
            if (a != null) {
                try {
                    repository.delete(a.getId());
                } catch (Exception e) {
                }
            }
        }
    }
}