package com.spring.bugs.server.domain;

public class Admin extends Employee {
    public Admin(String username, String password, SoftwareCompany company) {
        super(username, password, company);
    }

    public Admin() {
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", company=" + company +
                '}';
    }
}