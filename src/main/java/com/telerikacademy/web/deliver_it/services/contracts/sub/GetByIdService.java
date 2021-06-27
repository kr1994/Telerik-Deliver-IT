package com.telerikacademy.web.deliver_it.services.contracts.sub;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;

public interface GetByIdService<E> {
    E getById(int id, SecurityCredentials sc);
}
