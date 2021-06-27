package com.telerikacademy.web.deliver_it.repositories.contracts;

import com.telerikacademy.web.deliver_it.models.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends Repository<Customer> {

    List<Customer> get_And_Logic(
            Optional<String> firstName,
            Optional<String> lastName,
            Optional<String> email);

    List<Customer> get_Or_Logic(
            Optional<String> firstName,
            Optional<String> lastName,
            Optional<String> email);

    int numberOfCustomers();
}
