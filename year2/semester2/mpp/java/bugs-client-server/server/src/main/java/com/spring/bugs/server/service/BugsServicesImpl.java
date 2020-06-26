package com.spring.bugs.server.service;

import com.spring.bugs.server.domain.*;
import com.spring.bugs.server.repository.AppRepository;
import com.spring.bugs.server.repository.BugRepository;
import com.spring.bugs.server.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BugsServicesImpl implements BugsServices {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private BugRepository bugRepository;

    private List<BugsObserver> bugsObservers = new ArrayList<>();

    public BugsServicesImpl(EmployeeRepository employeeRepository, AppRepository appRepository, BugRepository bugRepository) {
        this.employeeRepository = employeeRepository;
        this.appRepository = appRepository;
        this.bugRepository = bugRepository;
    }

    @Override
    public synchronized Employee login(String username, String password) {
        try {
            List<Employee> employees = new ArrayList<>(employeeRepository.findByUsernameAndPassword(username, password));
            if (employees.size() == 0) {
                return null;
            }
            return employees.get(0);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public synchronized Employee addEmployee(String username, String type, SoftwareCompany company) {
        try {
            if (employeeRepository.findByUsername(username).size() != 0) {
                throw new ServiceException("Employee username already exists");
            }
            Employee e = null;
            String password = generateRandomPassword();
            if (type.equals("Admin")) {
                e = new Admin(username, password, company);
            } else if (type.equals("Developer")) {
                e = new Developer(username, password, company);
            } else if (type.equals("Tester")) {
                e = new Tester(username, password, company);
            }
            Employee employee = employeeRepository.save(e);

            ExecutorService executor = Executors.newFixedThreadPool(5);
            executor.execute(() -> {
                bugsObservers.forEach(o -> {
                    try {
                        o.updateEmployeeList();
                    } catch (Exception ex) {
                    }
                });
            });
            executor.shutdown();
            return employee;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public synchronized List<Employee> getAllEmployeesByCompany(SoftwareCompany company) {
        try {
            return employeeRepository.findAllByCompany(company);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public synchronized Employee updateEmployee(Employee employee, String password) {
        try {
            employee.setPassword(password);
            Employee e = employeeRepository.save(employee);
            ExecutorService executor = Executors.newFixedThreadPool(5);
            executor.execute(() -> {
                bugsObservers.forEach(o -> {
                    try {
                        o.updateEmployeeList();
                    } catch (Exception ex) {
                    }
                });
            });
            executor.shutdown();
            return e;
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public synchronized void deleteEmployee(Employee employee) {
        try {
            employee.setDeleted(true);
            employeeRepository.save(employee);
            ExecutorService executor = Executors.newFixedThreadPool(5);
            executor.execute(() -> {
                bugsObservers.forEach(o -> {
                    try {
                        o.updateEmployeeList();
                    } catch (Exception ex) {
                    }
                });
            });
            executor.shutdown();
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public synchronized List<App> getAllApps(SoftwareCompany company) {
        try {
            return appRepository.findAllByCompany(company);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public synchronized void registerBug(String name, String description, App app, Tester tester) {
        try {
            Bug bug = new Bug(name, description, LocalDateTime.now().withNano(0), tester, app);
            bugRepository.save(bug);
            ExecutorService executor = Executors.newFixedThreadPool(5);
            executor.execute(() -> {
                bugsObservers.forEach(o -> {
                    try {
                        o.updateBugsList();
                    } catch (Exception ex) {
                    }
                });
            });
            executor.shutdown();
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public synchronized List<Bug> getAllBugsOfApplication(App app) {
        try {
            return bugRepository.findAllByApp(app);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public synchronized void deleteBug(Bug bug) {
        try {
            bugRepository.delete(bug);
            ExecutorService executor = Executors.newFixedThreadPool(5);
            executor.execute(() -> {
                bugsObservers.forEach(o -> {
                    try {
                        o.updateBugsList();
                    } catch (Exception ex) {
                    }
                });
            });
            executor.shutdown();
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private String generateRandomPassword() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public synchronized void addObserver(BugsObserver o) {
        bugsObservers.add(o);
    }

    @Override
    public synchronized void removeObserver(BugsObserver o) {
        bugsObservers.remove(o);
    }

    @Override
    public synchronized List<Bug> getUnresolvedBugsOfApplication(App app) {
        return bugRepository.findUnresolvedByApp(app);
    }

    @Override
    public synchronized void resolveBug(Developer developer, Bug bug) {
        bug.setStatus(BugStatus.resolved);
        bug.setResolvedBy(developer);
        bug.setResolved(LocalDateTime.now().withNano(0));
        bugRepository.save(bug);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.execute(() -> {
            bugsObservers.forEach(o -> {
                try {
                    o.updateBugsList();
                } catch (Exception ex) {
                }
            });
        });
        executor.shutdown();
    }
}
