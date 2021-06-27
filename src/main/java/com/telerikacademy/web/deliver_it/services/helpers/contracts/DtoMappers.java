package com.telerikacademy.web.deliver_it.services.helpers.contracts;


import com.telerikacademy.web.deliver_it.models.Warehouse;
import com.telerikacademy.web.deliver_it.models.dto.AddressDto;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.*;

public interface DtoMappers extends AddressMapper, ParcelMapper, EmployeeMapper, CustomerMapper,
        ShipmentMapper, WarehouseMapper {
    Warehouse warehouseFromDto(AddressDto addressDto);
}
