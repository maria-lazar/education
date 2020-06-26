package com.spring.bugs.server.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class Developer extends Employee {
    public Developer(String username, String password, SoftwareCompany company) {
        super(username, password, company);
    }

    public Developer() {
    }

    @Override
    public String toString() {
        return "Developer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", company=" + company +
                '}';
    }
}