package com.spring.bugs.server.domain;

import java.io.Serializable;

public class Employee implements Serializable {
    protected Long id;
    protected String username;
    protected String password;
    protected boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    protected SoftwareCompany company;

    public Employee() {
    }

    public Employee(String username, String password, SoftwareCompany company) {
        this.username = username;
        this.password = password;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SoftwareCompany getCompany() {
        return company;
    }

    public void setCompany(SoftwareCompany company) {
        this.company = company;
    }
}