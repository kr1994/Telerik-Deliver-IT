package com.telerikacademy.web.deliver_it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "warehouses")
public class Warehouse implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private int warehouseId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Warehouse() {
    }

    public Warehouse(int warehouseId, Address address) {
        this.warehouseId = warehouseId;
        this.address = address;
    }

    @Override
    public int getId() {
        return warehouseId;
    }

    @Override
    public void setId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStreet1(){
        return getAddress().getStreet1();
    }

    public String getStreet2(){
        return getAddress().getStreet2();
    }

    public String getPostCode(){
        return getAddress().getPostCode();
    }

    public String getCityName(){
        return getAddress().getCityName();
    }

    public String getCountryName(){
        return getAddress().getCountryName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return getAddress().equals(warehouse.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(address.hashCode());
    }
}
