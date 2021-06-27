package com.telerikacademy.web.deliver_it.repositories.contracts;

import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.models.Warehouse;

import java.util.List;

public interface WarehouseRepository extends Repository<Warehouse>{

    List<Warehouse> getBy(Country country);

    List<Warehouse> getBy(City city);
}
