package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.models.Customer;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.services.CustomerServiceImpl;
import com.telerikacademy.web.deliver_it.services.contracts.sub.Create;
import com.telerikacademy.web.deliver_it.services.contracts.sub.DeleteService;
import com.telerikacademy.web.deliver_it.services.contracts.sub.GetByIdService;
import com.telerikacademy.web.deliver_it.services.contracts.sub.UpdateService;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

public interface CustomerService extends DeleteService<Customer>, GetByIdService<Customer>,
        UpdateService<Customer>, Create<Customer> {

    int numberOfCustomers();

    List<Customer> getAll(Optional<String> firstName,
                          Optional<String> lastName,
                          Optional<String> email,
                          Optional<String> any,
                          SecurityCredentials sc);

    List<Customer> getAll(SecurityCredentials sc);
}
