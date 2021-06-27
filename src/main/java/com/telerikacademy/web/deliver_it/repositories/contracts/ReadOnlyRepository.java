package com.telerikacademy.web.deliver_it.repositories.contracts;

import java.util.List;

public interface ReadOnlyRepository <E>{

    List<E> getAll();

    E getById(int id);

    E getByName(E entity);
}
