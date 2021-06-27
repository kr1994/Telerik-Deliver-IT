package com.telerikacademy.web.deliver_it.services;


import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.repositories.contracts.*;
import com.telerikacademy.web.deliver_it.services.contracts.CustomerService;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl extends ServiceBase<Customer> implements CustomerService {

    private final CustomerRepository customers;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customers, Security security) {
        super(customers,security);
        this.customers = customers;
    }

    @Override//to all
    public int numberOfCustomers() {
        return customers.numberOfCustomers();
    }

    @Override//emp
    public List<Customer> getAll(Optional<String> firstName, Optional<String> lastName,
                                 Optional<String> email, Optional<String> any, SecurityCredentials sc) {
        validateEmployee(sc);
        if (any.isPresent())
            return getByAny(any, firstName, lastName, email);

        return customers.get_And_Logic(firstName, lastName, email);
    }

    public List<Customer> getAll(SecurityCredentials sc) {
        validateEmployee(sc);

        return customers.getAll();
    }

    @Override//to all
    public Customer create(Customer customer) {
        validateUnique(customer);
        return customers.create(customer);
    }

    @Override//employee or self
    public Customer getById(int id, SecurityCredentials sc) {
        if (isEmployee(sc))
            return customers.getById(id);

        validateCustomer(id, sc);
        return customers.getById(id);
    }

    @Override//employee or self
    public Customer update(Customer customer,
                           SecurityCredentials sc) {
        if (isEmployee(sc))
            return customers.update(customer);

        Customer oldInfo = customers.getById(customer.getId());
        if (hasMach(oldInfo, sc))
            return customers.update(customer);
        throw new UnauthorizedException();
    }

    @Override//employee or self
    public Customer delete(int id, SecurityCredentials sc) {
        if (isEmployee(sc))
            return customers.delete(id);

        validateCustomer(id, sc);
        return customers.delete(id);
    }

    private List<Customer> getByAny(Optional<String> any, Optional<String> firstName,
                                    Optional<String> lastName, Optional<String> email) {
        if (firstName.isEmpty())
            firstName = any;
        if (lastName.isEmpty())
            lastName = any;
        if (email.isEmpty())
            email = any;
        return customers.get_Or_Logic(firstName, lastName, email);
    }

    private boolean hasMach(Customer customer, SecurityCredentials sc) {
        if (isCustomer(sc))
            return customer.getId() == sc.getId();
        return false;
    }
}
