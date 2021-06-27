package com.telerikacademy.web.deliver_it.services.helpers.contracts.sub;

import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.dto.ShipmentDto;

public interface ShipmentMapper {
    Shipment fromDto(ShipmentDto info);

    Shipment fromDto(int shipmentId, ShipmentDto info);
}
