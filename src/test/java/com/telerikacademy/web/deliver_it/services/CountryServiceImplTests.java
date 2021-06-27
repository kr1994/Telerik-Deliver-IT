package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.repositories.contracts.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.deliver_it.utils.Builders.buildCountry;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTests {

    @Mock
    CountryRepository countries;

    @InjectMocks
    CountryServiceImpl service;

    @Test
    public void getById_Should_CallRepository() {
        service.getById(MOCK_COUNTRY_ID);

        Mockito.verify(countries, Mockito.times(1))
                .getById(MOCK_COUNTRY_ID);
    }

    @Test
    public void getAll_Should_CallRepository() {
        service.getAll();

        Mockito.verify(countries, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getByName_Should_CallRepository() {
        Country country = buildCountry(COUNTRY_NAME);
        service.getByName(COUNTRY_NAME);

        Mockito.verify(countries, Mockito.times(1))
                .getByName(country);
    }

}
