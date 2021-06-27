package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.repositories.contracts.AddressRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.CountryRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.WarehouseRepository;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.mockWarehouseRepo_getById;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceImplTests {

    @Mock
    WarehouseRepository warehouses;

    @Mock
    CountryRepository countries;

    @Mock
    AddressRepository addresses;

    @Mock
    Security NEEDED_FOR_BASE_CLASS;

    @InjectMocks
    WarehouseServiceImpl service;

    @Test
    public void getAll_Should_Call_Repository() {
        service.getAll();
        Mockito.verify(warehouses, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Call_Repository() {
        service.getById(MOCK_WAREHOUSE_ID);

        Mockito.verify(warehouses, Mockito.times(1))
                .getById(MOCK_WAREHOUSE_ID);
    }

    @Test
    public void getByCountry_Should_ReturnExpectedAnswer() {
        List<Warehouse> expected = List.of(mockWarehouse());
        Country countryToSend = mockCountry();
        Mockito.when(countries.getByName(mockCountry()))
                .thenReturn(mockCountry());

        Mockito.when(warehouses.getBy(countryToSend))
                .thenReturn(expected);


        List<Warehouse> result = service.getByCountry(COUNTRY_NAME);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void create_Should_createWarehouse_With_CorrectInput() {
        Warehouse expected = MOCK_WH;
        Warehouse input = mockWarehouse();

        Mockito.when(addresses.put(input.getAddress()))
                .thenReturn(MOCK_WH_ADDRESS);
        Mockito.when(warehouses.getByName(expected))
                .thenThrow(EntityNotFoundException.class);

        service.create(input, SC_EMPLOYEE);

        Mockito.verify(warehouses, Mockito.times(1))
                .create(expected);
    }

    @Test
    public void create_Should_Throw_When_WarehouseExists() {
        Warehouse expected = mockWarehouse();
        Warehouse input = mockWarehouse();
        SecurityCredentials sc = mockSecurityCredentialEmployee();

        Mockito.when(warehouses.getByName(expected))
                .thenReturn(expected);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(input, sc));
    }

    @Test
    public void update_Should_updateWarehouseWithCorrectAddress() {
        Warehouse expected = MOCK_WH;
        Mockito.when(warehouses.getByName(expected))
                .thenThrow(EntityNotFoundException.class);
        mockWarehouseRepo_getById(MOCK_WAREHOUSE_ID, warehouses);

        Warehouse result = service.update(expected, SC_EMPLOYEE);

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(result.getId(), expected.getId());
        Assertions.assertEquals(result.getAddress().getId(),
                expected.getAddress().getId());
    }

    @Test
    public void update_Should_Throw_When_WarehouseDoesNotExists() {
        Warehouse expected = MOCK_WH;

        Mockito.when(warehouses.getByName(expected))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(warehouses.getById(expected.getId()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.update(expected, SC_EMPLOYEE));
    }

    @Test
    public void delete_Should_CallRepository() {
        int id = MOCK_WAREHOUSE_ID;
        SecurityCredentials sc = mockSecurityCredentialEmployee();

        service.delete(id, sc);

        Mockito.verify(warehouses, Mockito.times(1))
                .delete(id);
    }
}
