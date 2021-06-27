package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentService extends Service<Shipment> {

    List<Shipment> getByStatus(int warehouseId,
                               Optional<Integer> statusId,
                               SecurityCredentials sc);

    List<Shipment> getByCustomer(int customerId,
                                 SecurityCredentials sc);

    Shipment getNext(int warehouseId, SecurityCredentials sc);
}
