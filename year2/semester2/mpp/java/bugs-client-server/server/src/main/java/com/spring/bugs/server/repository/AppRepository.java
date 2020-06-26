package com.spring.bugs.server.repository;

import com.spring.bugs.server.domain.App;
import com.spring.bugs.server.domain.SoftwareCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppRepository extends JpaRepository<App, Long> {
    @Query("SELECT a FROM App a WHERE a.company=?1")
    List<App> findAllByCompany(SoftwareCompany company);
}
