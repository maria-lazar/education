package com.spring.bugs.server.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class Tester extends Employee {
    public Tester(String username, String password, SoftwareCompany company) {
        super(username, password, company);
    }

    public Tester() {
    }

    @Override
    public String toString() {
        return "Tester{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", company=" + company +
                '}';
    }
}