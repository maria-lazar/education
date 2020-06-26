package com.spring.bugs.server.domain;

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