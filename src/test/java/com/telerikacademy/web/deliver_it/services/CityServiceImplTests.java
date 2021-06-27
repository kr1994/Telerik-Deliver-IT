package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.repositories.contracts.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.deliver_it.utils.Builders.buildCity;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTests {

    @Mock
    CityRepository cities;

    @InjectMocks
    CityServiceImpl service;

    @Test
    public void getById_Should_CallRepository() {
        service.getById(MOCK_CITY_ID);

        Mockito.verify(cities, Mockito.times(1))
                .getById(MOCK_CITY_ID);
    }

    @Test
    public void getAll_Should_CallRepository() {
        service.getAll();

        Mockito.verify(cities, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getByName_Should_CallRepository() {
        City city = buildCity(CITY_NAME, COUNTRY_NAME);
        service.getByName(CITY_NAME, COUNTRY_NAME);

        Mockito.verify(cities, Mockito.times(1))
                .getByName(city);
    }
}
