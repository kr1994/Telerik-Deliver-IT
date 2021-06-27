package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.repositories.contracts.CountryRepository;
import com.telerikacademy.web.deliver_it.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Builders.buildCountry;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countries;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        countries = countryRepository;
    }

    @Override//for all
    public List<Country> getAll() {
        return countries.getAll();
    }

    @Override
    public List<Country> getCountriesForWarehouses(){
        return countries.getCountriesForWarehouses();
    }

    @Override
    public Country getById(int id) {
        return countries.getById(id);
    }

    @Override
    public Country getByName(String name){
        Country country = buildCountry(name);
        return countries.getByName(country);
    }
}
