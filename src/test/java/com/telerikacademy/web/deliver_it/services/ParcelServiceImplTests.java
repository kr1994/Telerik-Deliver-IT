package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.repositories.contracts.*;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.Filter;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.Sort;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.deliver_it.services.mock.MockSecurity.*;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.*;

@ExtendWith(MockitoExtension.class)
public class ParcelServiceImplTests {


    @Mock
    ParcelRepository parcels;

    @Mock
    Security security;

    @Mock
    Filter filter;

    @Mock
    Sort sort;

    @InjectMocks
    ParcelServiceImpl service;

    @Test
    public void getAllSorted_Should_CallSort_With_EmployeeCredentials() {
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isEmployee(sc, security);

        service.getAllSorted(NEW_PARCEL_SORTING_DTO, sc);

        Mockito.verify(sort, Mockito.times(1))
                .getAll(NEW_PARCEL_SORTING_DTO);
    }

    @Test
    public void getAllSorted_Should_CallSort_With_CustomerCredentialsAndMatchingCustomerId() {
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);
        ParcelSortingDto dto = new ParcelSortingDto();
        dto.setCustomerId(MOCK_CUSTOMER_ID);

        service.getAllSorted(dto, sc);

        Mockito.verify(sort, Mockito.times(1))
                .getAll(dto);
    }

    @Test
    public void getAllSorted_Should_Throw_With_WrongCredential() {
        mockSecurity_isEmployee(SC_WRONG, security);
        mockSecurity_isCustomer(SC_WRONG, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.getAllSorted(NEW_PARCEL_SORTING_DTO, SC_WRONG));
    }

    @Test
    public void filterByStatusFromShipment_Should_CallFilter_With_EmployeeCredentials() {
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isEmployee(sc, security);

        service.filterByStatusFromShipment(MOCK_SHIPMENT_STATUS_ID, sc);

        Mockito.verify(filter, Mockito.times(1))
                .byStatusMapToDto(mockStatus().getId());
    }

    @Test
    public void filterByStatusFromShipment_Should_CallFilter_With_CustomerCredentials() {
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);

        service.filterByStatusFromShipment(MOCK_SHIPMENT_STATUS_ID, sc);

        Mockito.verify(filter, Mockito.times(1))
                .customers_sOwnBy(MOCK_SHIPMENT_STATUS_ID, sc.getId());
    }

    @Test
    public void filterByStatusFromShipment_Should_Throw_With_WrongCredentials() {
        SecurityCredentials sc = SC_WRONG;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.filterByStatusFromShipment(MOCK_SHIPMENT_STATUS_ID, sc));
    }

    @Test
    public void filterByWeight_Should_CallFilter_With_EmployeeCredential() {
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isEmployee(sc, security);

        service.filterByWeight(NEW_FILTER_BY_WEIGHT_DTO, sc);

        Mockito.verify(filter, Mockito.times(1))
                .toDtoBy(NEW_FILTER_BY_WEIGHT_DTO);
    }

    @Test
    public void filterByWeight_Should_CallRepository_With_CustomerCredential() {
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);

        service.filterByWeight(NEW_FILTER_BY_WEIGHT_DTO, sc);

        Mockito.verify(filter, Mockito.times(1))
                .customers_sOwnBy(NEW_FILTER_BY_WEIGHT_DTO, sc.getId());
    }

    @Test
    public void filterByWeight_Should_Trow_With_WrongCredentials() {
        SecurityCredentials sc = SC_WRONG;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.filterByWeight(NEW_FILTER_BY_WEIGHT_DTO, sc));
    }

    @Test
    public void getStatusOfParcel_Should_CallFilter_With_CustomerCredentials() {
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isCustomer(sc, security);

        service.getStatusOfParcel(MOCK_PARCEL_ID, sc);

        Mockito.verify(filter, Mockito.times(1))
                .byCustomerAndParcel(MOCK_CUSTOMER_ID, MOCK_PARCEL_ID);
    }

    @Test
    public void getStatusOfParcel_Should_Throw_With_EmployeeCredentials() {
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isCustomer(sc, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.getStatusOfParcel(MOCK_PARCEL_ID, sc));
    }

    @Test
    public void create_Should_CallRepo_With_EmployeeCredential() {
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isEmployee(sc, security);

        Mockito.lenient().when(parcels.getById(MOCK_PARCEL_ID))
                .thenReturn(MOCK_PARCEL);

        Assertions.assertDoesNotThrow(() -> service.create(MOCK_PARCEL, sc));
    }

    @Test
    public void create_Should_Throw_With_CustomerCredential() {
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);

        Mockito.lenient().when(parcels.getById(MOCK_PARCEL_ID))
                .thenReturn(MOCK_PARCEL);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.create(MOCK_PARCEL, sc));
    }

    @Test
    public void update_Should_CallRepository() {
        Parcel expected = MOCK_PARCEL;

        service.update(expected, SC_EMPLOYEE);

        Mockito.verify(parcels, Mockito.times(1))
                .update(expected);
    }

    @Test
    public void delete_Should_CallRepository() {
        int id = MOCK_PARCEL_ID;
        SecurityCredentials sc = mockSecurityCredentialEmployee();

        service.delete(id, sc);

        Mockito.verify(parcels, Mockito.times(1))
                .delete(id);
    }

    @Test
    public void getById_Should_CallRepository_Whit_EmployeeCredentials() {
        mockSecurity_isEmployee(SC_EMPLOYEE, security);

        service.getById(MOCK_PARCEL_ID, SC_EMPLOYEE);

        Mockito.verify(parcels, Mockito.times(1))
                .getById(MOCK_PARCEL_ID);
    }

    @Test
    public void getById_Should_CallFilter_Whit_CustomerCredentials() {
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);

        service.getById(MOCK_PARCEL_ID, sc);

        Mockito.verify(filter, Mockito.times(1))
                .getParcelIfCustomer_sOwn(MOCK_PARCEL_ID, sc.getId());
    }

    @Test
    public void getById_Should_Throw_Whit_WrongCredentials() {
        SecurityCredentials sc = SC_WRONG;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.getById(MOCK_PARCEL_ID, sc));
    }
}
