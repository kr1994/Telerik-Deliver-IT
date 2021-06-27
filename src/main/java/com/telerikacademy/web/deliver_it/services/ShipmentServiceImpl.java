package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.ShipmentStatus;
import com.telerikacademy.web.deliver_it.models.Warehouse;
import com.telerikacademy.web.deliver_it.repositories.contracts.*;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.services.contracts.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.deliver_it.utils.Builders.buildStatus_Delivered;


@Service
public class ShipmentServiceImpl extends ServiceBase<Shipment> implements ShipmentService {

    private final ReadOnlyRepository<Warehouse> warehouses;
    private final ShipmentRepository shipments;

    @Autowired
    public ShipmentServiceImpl(WarehouseRepository warehouses,
                               ShipmentRepository shipments,
                               Security security) {
        super(shipments,security);
        this.shipments = shipments;
        this.warehouses = warehouses;
    }

    @Override//employee
    public List<Shipment> getByCustomer(int customerId, SecurityCredentials sc) {
        validateEmployee(sc);
        return shipments.getByCustomer(customerId);
    }


    @Override//employee
    public List<Shipment> getByStatus(int warehouseId, Optional<Integer> statusId,
                                      SecurityCredentials sc) {
        validateEmployee(sc);
        return shipments.getByStatus(warehouseId, statusId);
    }

    @Override//employee
    public Shipment getNext(int warehouseId, SecurityCredentials sc) {
        validateEmployee(sc);
        Warehouse warehouse = warehouses.getById(warehouseId);
        Date currentDate = new Date();
        ShipmentStatus delivered = buildStatus_Delivered();
        return shipments.getNext(warehouse, currentDate, delivered);
    }
}
