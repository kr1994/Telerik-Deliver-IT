package com.telerikacademy.web.deliver_it.repositories.contracts;

import com.telerikacademy.web.deliver_it.models.Country;

import java.util.List;


public interface CountryRepository extends Repository<Country> {
   List<Country> getCountriesForWarehouses();
}
