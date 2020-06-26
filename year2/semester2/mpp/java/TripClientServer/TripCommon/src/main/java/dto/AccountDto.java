package dto;

import java.io.Serializable;

public class AccountDto implements Serializable {
    public String name;
    public String password;
    public final Integer id;

    public AccountDto(String name, String password) {
        this(null, name, password);
    }

    public AccountDto(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public AccountDto(Integer accountId) {
        id = accountId;
    }
}
