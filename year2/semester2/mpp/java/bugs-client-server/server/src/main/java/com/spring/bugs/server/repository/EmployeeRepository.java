package com.spring.bugs.server.repository;

import com.spring.bugs.server.domain.Employee;
import com.spring.bugs.server.domain.SoftwareCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.username=?1 and e.password=?2 and e.deleted=false")
    Collection<Employee> findByUsernameAndPassword(String username, String password);

    @Query("SELECT e FROM Employee e WHERE e.username=?1")
    Collection<Employee> findByUsername(String username);

    @Query("SELECT e FROM Employee e WHERE e.company=?1 and e.deleted=false")
    List<Employee> findAllByCompany(SoftwareCompany company);

}
