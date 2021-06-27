package com.telerikacademy.web.deliver_it.services.contracts.sub;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;

public interface CreateService<E> {
    E create(E entity, SecurityCredentials se);
}
