package com.spring.bugs.server.service;

import com.spring.bugs.server.domain.*;

import java.util.List;

public interface BugsServices {
    Employee login(String username, String password);

    Employee addEmployee(String username, String type, SoftwareCompany company);

    List<Employee> getAllEmployeesByCompany(SoftwareCompany company);

    Employee updateEmployee(Employee employee, String password);

    void deleteEmployee(Employee employee);

    List<App> getAllApps(SoftwareCompany company);

    void registerBug(String name, String description, App app, Tester tester);

    List<Bug> getAllBugsOfApplication(App app);

    void deleteBug(Bug bug);

    void addObserver(BugsObserver observer);

    void removeObserver(BugsObserver observer);

    List<Bug> getUnresolvedBugsOfApplication(App app);

    void resolveBug(Developer developer, Bug bug);
}
