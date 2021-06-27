package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.WarehouseRepository;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.mockWarehouseRepo_getById;
import static com.telerikacademy.web.deliver_it.services.mock.MockSecurity.mockSecurity_validateEmployeeThrow;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceImplTests {

    @Mock
    WarehouseRepository warehouses;

    @Mock
    ShipmentRepository shipments;

    @Mock
    Security security;

    @InjectMocks
    ShipmentServiceImpl service;

    @Test
    public void getByCustomer_Should_CallRepository_Whit_EmployeeCredential() {
        service.getByCustomer(MOCK_CUSTOMER_ID, SC_EMPLOYEE);

        Mockito.verify(shipments, Mockito.times(1))
                .getByCustomer(MOCK_CUSTOMER_ID);
    }

    @Test
    public void getByStatus_Should_CallRepository_With_EmployeeCredential() {
        service.getByStatus(MOCK_WAREHOUSE_ID, Optional.of(MOCK_SHIPMENT_STATUS_ID), SC_EMPLOYEE);

        Mockito.verify(shipments, Mockito.times(1))
                .getByStatus(MOCK_WAREHOUSE_ID, Optional.of(MOCK_SHIPMENT_STATUS_ID));
    }

    @Test
    public void getNext_Should_CallRepository_With_EmployeeCredential() {
        mockWarehouseRepo_getById(MOCK_WAREHOUSE_ID, warehouses);
        when(shipments.getNext(any(Warehouse.class), any(Date.class), any(ShipmentStatus.class)))
                .thenReturn(MOCK_SHIPMENT);

        Shipment result = service.getNext(MOCK_WAREHOUSE_ID, SC_EMPLOYEE);

        Assertions.assertEquals(MOCK_SHIPMENT, result);
    }

    @Test
    public void getById_Should_CallRepository_With_EmployeeCredential() {
        service.getById(MOCK_SHIPMENT_ID, SC_EMPLOYEE);

        verify(shipments, times(1))
                .getById(MOCK_SHIPMENT_ID);
    }

    @Test
    public void create_Should_createShipment_With_CorrectData() {
        Shipment shipment = MOCK_SHIPMENT;
        Mockito.when(shipments.getByName(shipment))
                .thenThrow(EntityNotFoundException.class);

        service.create(shipment, SC_EMPLOYEE);

        Mockito.verify(shipments, Mockito.times(1))
                .create(shipment);
    }

    @Test
    public void create_Should_Throw_When_ShipmentExists() {
        Shipment expected = MOCK_SHIPMENT;

        Mockito.when(shipments.getByName(expected))
                .thenReturn(expected);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(expected, SC_EMPLOYEE));
    }

    @Test
    public void update_Should_CallRepository() {
        Shipment expected = MOCK_SHIPMENT;

        service.update(expected, SC_EMPLOYEE);

        Mockito.verify(shipments, Mockito.times(1))
                .update(expected);
    }

    @Test
    public void delete_Should_CallRepository() {
        int id = MOCK_SHIPMENT_ID;

        service.delete(id, SC_EMPLOYEE);

        Mockito.verify(shipments, Mockito.times(1))
                .delete(id);
    }

    @Test
    public void delete_Should_Throw_With_WrongCredential() {
        mockSecurity_validateEmployeeThrow(SC_WRONG, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.delete(MOCK_SHIPMENT_ID, SC_WRONG));
    }
}
