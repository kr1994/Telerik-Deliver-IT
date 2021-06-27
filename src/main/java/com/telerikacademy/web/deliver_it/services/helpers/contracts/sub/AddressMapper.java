package com.telerikacademy.web.deliver_it.services.helpers.contracts.sub;

import com.telerikacademy.web.deliver_it.models.Address;
import com.telerikacademy.web.deliver_it.models.dto.AddressDto;

public interface AddressMapper {
    Address fromDto(AddressDto info);

    Address fromDto(int id, AddressDto info);
}
