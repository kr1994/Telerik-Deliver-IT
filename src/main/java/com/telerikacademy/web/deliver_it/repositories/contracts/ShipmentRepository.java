package com.telerikacademy.web.deliver_it.repositories.contracts;

import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.ShipmentStatus;
import com.telerikacademy.web.deliver_it.models.Warehouse;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends Repository<Shipment>{

    List<Shipment> getByWarehouse(int warehouseId);

    List<Shipment> getByCustomer(int customerId);

    List<Shipment> getByStatus(int warehouseId,
                               Optional<Integer> statusId);

    List<Shipment> getAll(ParcelSortingDto dto);

    Shipment getNext(Warehouse warehouse, Date currentDate, ShipmentStatus status);

}
