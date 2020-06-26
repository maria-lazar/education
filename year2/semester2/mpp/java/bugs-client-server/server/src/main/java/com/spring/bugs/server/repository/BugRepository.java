package com.spring.bugs.server.repository;

import com.spring.bugs.server.domain.App;
import com.spring.bugs.server.domain.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    @Query("SELECT b FROM Bug b WHERE b.application=?1")
    List<Bug> findAllByApp(App app);

    @Query("SELECT b FROM Bug b WHERE b.application=?1 and b.status=0")
    List<Bug> findUnresolvedByApp(App app);
}
