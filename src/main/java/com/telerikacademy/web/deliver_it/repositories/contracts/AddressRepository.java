package com.telerikacademy.web.deliver_it.repositories.contracts;

import com.telerikacademy.web.deliver_it.models.Address;


public interface AddressRepository extends Repository<Address> {

    Address getByUserId(int userId);
}
