package com.spring.bugs.server.domain;

import org.hibernate.annotations.Entity;
import javax.persistance.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Bug implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private BugStatus status;
    private LocalDateTime resolved;
    private LocalDateTime created;

    @ManyToOne(targetEntity = Developer.class)
    @JoinColumn(name = "resolver_id")
    private Developer resolvedBy;

    @ManyToOne(targetEntity = Tester.class)
    @JoinColumn(name = "registerer_id")
    private Tester registeredBy;

    @ManyToOne(targetEntity = App.class)
    @JoinColumn(name = "application_id")
    private App application;

    public Bug() {
    }

    public Bug(String title, String description, LocalDateTime created, Tester registeredBy, App application) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.registeredBy = registeredBy;
        this.application = application;
        this.status = BugStatus.unresolved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BugStatus getStatus() {
        return status;
    }

    public void setStatus(BugStatus status) {
        this.status = status;
    }

    public LocalDateTime getResolved() {
        return resolved;
    }

    public void setResolved(LocalDateTime resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Developer getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(Developer resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Tester getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(Tester registeredBy) {
        this.registeredBy = registeredBy;
    }

    public App getApplication() {
        return application;
    }

    public void setApplication(App application) {
        this.application = application;
    }
}