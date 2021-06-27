package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.services.contracts.sub.CreateService;
import com.telerikacademy.web.deliver_it.services.contracts.sub.DeleteService;
import com.telerikacademy.web.deliver_it.services.contracts.sub.GetByIdService;
import com.telerikacademy.web.deliver_it.services.contracts.sub.UpdateService;

public interface Service<E> extends CreateService<E>, UpdateService<E>,
        DeleteService<E>, GetByIdService<E> {
}
