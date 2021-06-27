package com.telerikacademy.web.deliver_it.services.helpers.contracts.sub;

import com.telerikacademy.web.deliver_it.models.Customer;
import com.telerikacademy.web.deliver_it.models.dto.CustomerDto;

public interface CustomerMapper {
    Customer fromDto(CustomerDto info);

    Customer fromDto(int customerId, CustomerDto info);
}
