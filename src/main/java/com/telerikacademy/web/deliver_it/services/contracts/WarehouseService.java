package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.models.Warehouse;
import com.telerikacademy.web.deliver_it.services.contracts.sub.*;

import java.util.List;

public interface WarehouseService extends DeleteService<Warehouse>, GetById<Warehouse>,
        UpdateService<Warehouse>, GetAll<Warehouse>, CreateService<Warehouse> {

    List<Warehouse> getByCountry(String countryName);
}
