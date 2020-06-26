package repository;


import domain.Entity;

import java.util.List;

public interface CrudRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);

    List<E> findAll();

    E save(E e);

    E delete(ID id);

    int size();
}
