package repository;

import domain.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import validator.ValidationException;

import java.util.List;

public class AccountRepositoryOrm extends AccountRepository {
    private static final Logger LOGGER = LogManager.getLogger(AccountRepositoryOrm.class.getName());

    public AccountRepositoryOrm() {
    }

    @Override
    public Account findOne(Integer id) {
        LOGGER.traceEntry("finding account with id {}", id);
        if (id == null) {
            LOGGER.warn("findOne failed null id");
            throw new IllegalArgumentException("Cannot find account: id must be not null");
        }
        try (Session session = TripsSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Account account = session.createQuery("from Account as a where a.id=:id", Account.class)
                        .setParameter("id", id)
                        .uniqueResult();
                tx.commit();
                if (account == null) {
                    LOGGER.traceExit("no account found with id {}", id);
                }
                return account;
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new ValidationException(ex);
            }
        }
    }

    @Override
    public List<Account> findAll() {
        LOGGER.traceEntry("findAll accounts");
        try (Session session = TripsSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Account> accounts = session.createQuery("from Account", Account.class)
                        .list();
                tx.commit();
                LOGGER.traceExit();
                return accounts;
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
                LOGGER.warn(ex);
                throw new ValidationException(ex);
            }
        }
    }

    @Override
    public Account save(Account account) {
        LOGGER.traceEntry("saving {}", account);
        if (account == null) {
            LOGGER.warn("save failed null account");
            throw new IllegalArgumentException("Save failed account must be not null");
        }
        try (Session session = TripsSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                if (account.getId() == null) {
                    LOGGER.info("inserting {}", account);
                    tx = session.beginTransaction();
                    int id = (int) session.save(account);
                    account.setId(id);
                    tx.commit();
                    LOGGER.traceExit(account);
                    return account;
                } else {
                    LOGGER.info("updating {}", account);
                    tx = session.beginTransaction();
                    Account account1 = findOne(account.getId());
                    if (account1 == null) {
                        throw new ValidationException("Update failed accound doesn't exist");
                    }
                    session.update(account);
                    tx.commit();
                    LOGGER.traceExit(account);
                    return account;
                }
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
                LOGGER.warn(ex);
                throw new ValidationException(ex);
            }
        }
    }

    @Override
    public Account delete(Integer id) {
        LOGGER.traceEntry("delete account with id {}", id);
        if (id == null) {
            LOGGER.warn("delete failed null id");
            throw new IllegalArgumentException("Delete failed account id must be not null");
        }
        try (Session session = TripsSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Account account = findOne(id);
                if (account == null) {
                    LOGGER.warn("delete failed account with id {} not found", id);
                    throw new ValidationException("Delete failed account not found");
                }
                session.delete(account);
                tx.commit();
                LOGGER.traceExit(account);
                return account;

            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
                LOGGER.warn(ex);
                throw new ValidationException(ex);
            }
        }
    }

    @Override
    public int size() {
        LOGGER.traceEntry("size");
        try (Session session = TripsSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                long s = (long) session.createQuery("select count(*) from Account").uniqueResult();
                tx.commit();
                return Math.toIntExact(s);
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
                LOGGER.warn(ex);
                throw new ValidationException(ex);
            }
        }
    }

    @Override
    public Account findByNamePassword(String name, String password) {
        LOGGER.traceEntry("finding account with name {} and password {}", name, password);
        if (name == null || password == null) {
            LOGGER.warn("find by name and password failed null data");
            throw new IllegalArgumentException("Cannot find account: name and password must be not null");
        }
        try (Session session = TripsSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Account account = session.createQuery("from Account as m where name=:name and password=:password", Account.class)
                        .setParameter("name", name)
                        .setParameter("password", password)
                        .uniqueResult();
                tx.commit();
                LOGGER.traceExit(account);
                return account;
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
                LOGGER.warn(ex);
                throw new ValidationException(ex);
            }
        }
    }
}
