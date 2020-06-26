package com.spring.bugs.server.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "application")
public class App implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(targetEntity = SoftwareCompany.class)
    @JoinColumn(name = "software_company_id")
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