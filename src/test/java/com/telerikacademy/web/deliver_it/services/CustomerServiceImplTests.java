package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.Customer;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.repositories.contracts.CustomerRepository;
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

import java.util.Optional;

import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.mockCustomerRepo_getById;
import static com.telerikacademy.web.deliver_it.services.mock.MockSecurity.*;
import static com.telerikacademy.web.deliver_it.utils.Builders.fillUserInfo;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTests {

    @Mock
    CustomerRepository customers;

    @Mock
    Security security;

    @InjectMocks
    CustomerServiceImpl service;


    @Test
    public void numberOfCustomers_Should_CallRepository() {
        service.numberOfCustomers();
        Mockito.verify(customers, Mockito.times(1))
                .numberOfCustomers();
    }

    @Test
    public void getAll_Should_CallRepository_When_AnyIsEmpty() {
        Optional<String> st = Optional.of(Mockito.anyString());
        Optional<String> empty = Optional.empty();

        service.getAll(st, st, st, empty, SC_EMPLOYEE);

        Mockito.verify(customers, Mockito.times(1))
                .get_And_Logic(st, st, st);
    }

    @Test
    public void getAll_Should_CallRepository_With_CorrectData_When_AnyIsPresent() {
        SecurityCredentials sc = mockSecurityCredentialEmployee();
        Optional<String> any = Optional.of(Mockito.anyString());
        Optional<String> empty = Optional.empty();

        service.getAll(empty, empty, empty, any, sc);

        Mockito.verify(customers, Mockito.times(1))
                .get_Or_Logic(any, any, any);
    }

    @Test
    public void create_Should_CallRepository_When_CustomerIsUnique() {
        Customer customer = MOCK_CUSTOMER;
        Mockito.when(customers.getByName(customer))
                .thenThrow(EntityNotFoundException.class);

        service.create(customer);

        Mockito.verify(customers, Mockito.times(1))
                .create(customer);
    }

    @Test
    public void create_Should_Throw_When_CustomerExists() {
        Customer customer = MOCK_CUSTOMER;

        Mockito.when(customers.getByName(customer))
                .thenReturn(customer);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(customer));
    }

    @Test
    public void getById_Should_CallRepository_With_EmployeeCredentials() {
        int id = MOCK_CUSTOMER_ID;
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isEmployee(sc, security);

        service.getById(id, sc);

        Mockito.verify(customers, Mockito.times(1))
                .getById(id);
    }

    @Test
    public void getById_Should_CallRepository_When_SecurityMatchesForCustomer() {
        int id = MOCK_CUSTOMER_ID;
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);

        service.getById(id, sc);

        Mockito.verify(customers, Mockito.times(1))
                .getById(id);
    }

    @Test
    public void getById_Should_Throw_When_SecurityFailsForCustomer() {
        int id = MOCK_CUSTOMER_ID;
        SecurityCredentials sc = SC_WRONG;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_validateCustomerThrow(id, sc, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.getById(id, sc));
    }

    @Test
    public void update_Should_CallRepository_With_EmployeeCredentials() {
        Customer input = mockCustomer();
        fillUserInfo(input, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_EMAIL);
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isEmployee(sc, security);

        service.update(input, sc);

        Mockito.verify(customers, times(1))
                .update(input);
    }

    @Test
    public void update_Should_CallRepository_With_CustomerCredentials() {
        Customer input = mockCustomer();
        fillUserInfo(input, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_EMAIL);
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);
        mockCustomerRepo_getById(MOCK_CUSTOMER_ID, customers);

        service.update(input, sc);

        Mockito.verify(customers, times(1))
                .update(input);
    }

    @Test
    public void update_Should_Throw_With_WrongCredentials() {
        Customer input = mockCustomer();
        fillUserInfo(input, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_EMAIL);
        SecurityCredentials sc = SC_WRONG;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_isCustomer(sc, security);
        mockCustomerRepo_getById(MOCK_CUSTOMER_ID, customers);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.update(input, sc));
    }

    @Test
    public void delete_Should_CallRepository_With_EmployeeCredentials() {
        int input = MOCK_CUSTOMER_ID;
        SecurityCredentials sc = SC_EMPLOYEE;
        mockSecurity_isEmployee(sc, security);

        service.delete(input, sc);

        Mockito.verify(customers, times(1))
                .delete(input);
    }

    @Test
    public void delete_Should_CallRepository_With_CustomerCredentials() {
        int input = MOCK_CUSTOMER_ID;
        SecurityCredentials sc = SC_CUSTOMER;
        mockSecurity_isEmployee(sc, security);

        service.delete(input, sc);

        Mockito.verify(customers, times(1))
                .delete(input);
    }

    @Test
    public void delete_Should_Throw_With_WrongCredentials() {
        int input = MOCK_CUSTOMER_ID;
        SecurityCredentials sc = SC_WRONG;
        mockSecurity_isEmployee(sc, security);
        mockSecurity_validateCustomerThrow(input, sc, security);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.delete(input, sc));
    }
}
