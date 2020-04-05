package repository;

import domain.Entity;
import validators.Validator;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, T extends Entity<ID>> implements CrudRepository<ID, T> {
    protected Map<ID, T> entities;
    protected Validator<T> validator;

    public InMemoryRepository(Validator<T> validator) {
        this.validator = validator;
        this.entities = new HashMap<>();
    }

    @Override
    public T findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must be not null");
        }
        if (entities.containsKey(id)) {
            return entities.get(id);
        }
        return null;
    }

    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must be not null");
        }
        validator.validate(entity);
        if (entities.containsKey(entity.getId())) {
            return entity;
        }
        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public T delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be not null");
        }
        if (!entities.containsKey(id)) {
            return null;
        }
        T e = entities.get(id);
        entities.remove(id);
        return e;
    }

    @Override
    public T update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must be not null");
        }
        validator.validate(entity);
        if (!entities.containsKey(entity.getId())) {
            return entity;
        }
        entities.replace(entity.getId(), entity);
        return null;
    }
}
