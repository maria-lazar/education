package com.spring.bugs.server.domain;


import java.io.Serializable;

public class App implements Serializable {
    private Long id;
    private String name;

    private SoftwareCompany company;

    public App() {
    }

    public App(String name, SoftwareCompany company) {
        this.name = name;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoftwareCompany getCompany() {
        return company;
    }

    public void setCompany(SoftwareCompany company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return name;
    }
}