package com.telerikacademy.web.deliver_it.services.helpers.contracts.sub;

import com.telerikacademy.web.deliver_it.models.Warehouse;
import com.telerikacademy.web.deliver_it.models.dto.AddressDto;

public interface WarehouseMapper {
    Warehouse warehouseFromDto(int warehouseId, AddressDto addressDto);
}
