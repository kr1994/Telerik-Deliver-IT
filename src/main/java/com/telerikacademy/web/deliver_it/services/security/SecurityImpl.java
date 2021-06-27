package com.telerikacademy.web.deliver_it.services.security;

import com.telerikacademy.web.deliver_it.repositories.contracts.CustomerRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.EmployeeRepository;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import com.telerikacademy.web.deliver_it.models.Customer;
import com.telerikacademy.web.deliver_it.models.Employee;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.User;
import com.telerikacademy.web.deliver_it.repositories.contracts.ReadOnlyRepository;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.generatePassHash;


@Service
public class SecurityImpl implements Security {

    private final ReadOnlyRepository<Employee> employees;
    private final ReadOnlyRepository<Customer> customers;

    @Autowired
    public SecurityImpl(EmployeeRepository employees,
                        CustomerRepository customers) {
        this.employees = employees;
        this.customers = customers;
    }

    @Override
    public boolean isEmployee(SecurityCredentials sc) {
        try {
            validateEmployee(sc);
            return true;
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    @Override
    public boolean isCustomer(SecurityCredentials sc) {
        try {
            validateCustomer(sc);
            return true;
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    @Override
    public void validateEmployee(SecurityCredentials sc) {
        Employee employee = getUser(sc.getId(), employees);
        if (failsSecurityChekForEmployee(sc, employee))
            throw new UnauthorizedException();
    }

    @Override
    public void validateCustomer(SecurityCredentials credentials) {
        Customer customer = getUser(credentials.getId(), customers);
        if (failsSecurityChek(credentials, customer))
            throw new UnauthorizedException();
    }

    @Override
    public void validateCustomer(int idOfCustomerToCheck, SecurityCredentials sc) {
        if (idOfCustomerToCheck != sc.getId())
            throw new UnauthorizedException();

        Customer customer = getUser(idOfCustomerToCheck, customers);
        if (failsSecurityChek(sc, customer))
            throw new UnauthorizedException();
    }

    private <E> E getUser(int id, ReadOnlyRepository<E> repo) {
        try {
            return repo.getById(id);
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedException();
        }
    }

    private boolean failsSecurityChekForEmployee(SecurityCredentials sc, Employee employee) {
        return passwordFails(sc, employee) ||
                failsSecurityChek(sc, employee);
    }

    private boolean failsSecurityChek(SecurityCredentials sc, User user) {
        return !user.getFirstName().equals(sc.getName());//Id is used to get user
    }

    private boolean passwordFails(SecurityCredentials sc, Employee employee) {
        return !employee.getPassHash().equals(
                generatePassHash(sc.getPassword(), employee.getSalt()));
    }
}
