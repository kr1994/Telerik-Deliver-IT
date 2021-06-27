package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.repositories.contracts.AddressRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.CountryRepository;
import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.repositories.contracts.Repository;
import com.telerikacademy.web.deliver_it.repositories.contracts.WarehouseRepository;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.services.contracts.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WarehouseServiceImpl extends ServiceBase<Warehouse> implements WarehouseService {

    private final WarehouseRepository warehouses;
    private final CountryRepository countries;
    private final Repository<Address> addresses;

    @Autowired
    public WarehouseServiceImpl(Security security, WarehouseRepository warehouse, CountryRepository countries,
                                AddressRepository addresses) {
        super(warehouse, security);
        this.warehouses = warehouse;
        this.countries = countries;
        this.addresses = addresses;
    }

    @Override//to all
    public List<Warehouse> getAll() {
        return warehouses.getAll();
    }

    @Override//to all
    public List<Warehouse> getByCountry(String name) {
        Country country = findCountry(name);
        return warehouses.getBy(country);
    }

    @Override//emp, put address
    public Warehouse create(Warehouse warehouse, SecurityCredentials sc) {
        validateEmployee(sc);
        validateUnique(warehouse);

        Address fromRepo = addresses.put(warehouse.getAddress());
        warehouse.setAddress(fromRepo);

        return warehouses.create(warehouse);
    }

    @Override//emp, update address
    public Warehouse update(Warehouse warehouse, SecurityCredentials sc){
        validateEmployee(sc);
        validateUnique(warehouse);
        addresses.update(warehouse.getAddress());
        return getById(warehouse.getId());
    }

    @Override//to all
    public Warehouse getById(int id) {
        return warehouses.getById(id);
    }

    private Country findCountry(String name) {
        Country country = new Country();
        country.setCountryName(name);
        return countries.getByName(country);
    }

}