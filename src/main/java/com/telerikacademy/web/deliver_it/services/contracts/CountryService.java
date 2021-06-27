package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.services.contracts.sub.GetAll;
import com.telerikacademy.web.deliver_it.services.contracts.sub.GetById;

import java.util.List;

public interface CountryService extends GetById<Country>, GetAll<Country> {

    List<Country> getCountriesForWarehouses();

    Country getByName(String name);
}
