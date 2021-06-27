package com.telerikacademy.web.deliver_it.services.contracts.sub;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;

public interface UpdateService<E> {

    E update(E entity, SecurityCredentials sc);
}
