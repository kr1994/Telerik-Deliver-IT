package com.telerikacademy.web.deliver_it.services.contracts.sub;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;

import java.util.List;

public interface GetAllService <E>{
    List<E> getAll(SecurityCredentials sc);
}
