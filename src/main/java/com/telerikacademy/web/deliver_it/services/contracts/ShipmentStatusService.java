package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.models.ShipmentStatus;
import com.telerikacademy.web.deliver_it.services.contracts.sub.GetAllService;

public interface ShipmentStatusService
        extends Service<ShipmentStatus>,
        GetAllService<ShipmentStatus> {
}
