package com.telerikacademy.web.deliver_it.repositories.contracts;

public interface Repository <E> extends ReadOnlyRepository<E>{

    E create(E entity);

    E update(E entity);

    E delete(int id);

    E delete(E entity);

    E put(E entity);
}
