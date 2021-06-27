package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.repositories.contracts.CityRepository;
import com.telerikacademy.web.deliver_it.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Builders.buildCity;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cities;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        cities = cityRepository;
    }

    @Override
    public List<City> getAll() {
        return cities.getAll();
    }

    @Override
    public City getById(int id) {
        return cities.getById(id);
    }

    @Override
    public City getByName(String cityName, String countryName) {
        City city = buildCity(cityName, countryName);

        return cities.getByName(city);
    }

}
