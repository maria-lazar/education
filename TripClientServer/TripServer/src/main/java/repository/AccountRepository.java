package repository;

import domain.Account;
import myjdbc.MyJdbcTemplate;
import myjdbc.ResultSetExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validator.ValidationException;

import java.util.List;

public class AccountRepository implements CrudRepository<Integer, Account> {
    private MyJdbcTemplate template;
    private static final Logger LOGGER = LogManager.getLogger(AccountRepository.class.getName());

    public AccountRepository(MyJdbcTemplate template) {
        this.template = template;
    }

    public AccountRepository() {
    }

    @Override
    public Account findOne(Integer id) {
        LOGGER.traceEntry("finding account with id {}", id);
        if (id == null) {
            LOGGER.warn("findOne failed null id");
            throw new IllegalArgumentException("Cannot find account: id must be not null");
        }
        List<Account> list = template.query(
                String.format("SELECT account_id, name, password FROM Account WHERE account_id=%d", id),
                getAccountResultSetExtractor());
        if (list.isEmpty()) {
            LOGGER.traceExit("no account found with id {}", id);
            return null;
        }
        LOGGER.traceExit(list.get(0));
        return list.get(0);
    }

    @Override
    public List<Account> findAll() {
        LOGGER.traceEntry("findAll accounts");
        LOGGER.traceExit();
        return template.query("SELECT account_id, name, password FROM Account",
                getAccountResultSetExtractor());
    }

    @Override
    public Account save(Account account) {
        LOGGER.traceEntry("saving {}", account);
        if (account == null) {
            LOGGER.warn("save failed null account");
            throw new IllegalArgumentException("Update failed account must be not null");
        }
        if (account.getId() == null) {
            LOGGER.info("inserting {}", account);
            int id = template.insert(
                    String.format("INSERT INTO Account(name, password) VALUES('%s', '%s')",
                            account.getName(),
                            account.getPassword()));
            account.setId(id);
            LOGGER.traceExit(account);
            return account;
        }
        LOGGER.info("updating {}", account);
        int lines = template.update(
                String.format("UPDATE Account SET name='%s', password='%s' WHERE account_id=%d",
                        account.getName(),
                        account.getPassword(),
                        account.getId()));
        if (lines == 0) {
            LOGGER.warn("updating failed account not found");
            throw new ValidationException("Update failed account not found");
        }
        LOGGER.traceExit(account);
        return account;
    }

    @Override
    public Account delete(Integer id) {
        LOGGER.traceEntry("delete account with id {}", id);
        if (id == null){
            LOGGER.warn("delete failed null id");
            throw new IllegalArgumentException("Delete failed account id must be not null");
        }
        Account account = findOne(id);
        if (account == null){
            LOGGER.warn("delete failed account with id {} not found", id);
            throw new ValidationException("Delete failed account not found");
        }
        int lines = template.update(String.format("DELETE FROM Account WHERE account_id=%d", id));
        if (lines == 0){
            LOGGER.warn("delete failed for account with id {}", id);
            throw new ValidationException("Delete account failed");
        }
        LOGGER.traceExit(account);
        return account;
    }

    @Override
    public int size() {
        LOGGER.traceEntry("size");
        int size = template.query("SELECT COUNT(*) FROM Account", (rs) -> rs.getInt(1)).get(0);
        LOGGER.traceExit(size);
        return size;
    }

    private ResultSetExtractor<Account> getAccountResultSetExtractor() {
        return (rs) -> new Account(
                rs.getInt("account_id"),
                rs.getString("name"),
                rs.getString("password"));
    }

    public Account findByNamePassword(String name, String password){
        LOGGER.traceEntry("finding account with name {} and password {}", name, password);
        if (name == null || password == null) {
            LOGGER.warn("find by name and password failed null data");
            throw new IllegalArgumentException("Cannot find account: name and password must be not null");
        }
        List<Account> list = template.query(
                String.format("SELECT account_id, name, password FROM Account WHERE name='%s' AND password='%s'", name, password),
                getAccountResultSetExtractor());
        if (list.isEmpty()) {
            LOGGER.warn("no account found with name {} and password {}", name, password);
            LOGGER.traceExit(null);
            return null;
        }
        LOGGER.traceExit(list.get(0));
        return list.get(0);
    }

}
