package com.telerikacademy.web.deliver_it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Entity
@Table(name = "parcels")
public class Parcel implements IdentifyAble{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parcel_id")
    private int parcelId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse destination;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parcel_category_id")
    private ParcelCategory category;

    @JsonIgnore
    @Column(name = "weight_amount")
    private double weight;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "weight_units_id")
    private WeightUnit weightUnit;

    @Column(name = "shipment_id")
    private int shipmentId;

    public Parcel() {
    }

    @Override
    public int getId() {
        return parcelId;
    }

    @Override
    public void setId(int parcelId) {
        this.parcelId = parcelId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Warehouse getDestination() {
        return destination;
    }

    public void setDestination(Warehouse destination) {
        this.destination = destination;
    }

    public ParcelCategory getCategory() {
        return category;
    }

    public void setCategory(ParcelCategory category) {
        this.category = category;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getWeightInfo() {
        return String.format("%.2f %s", getWeight(), getWeightUnit().toString());
    }

    public String getCategoryInfo() {
        return getCategory().toString();
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return getId() == parcel.getId() &&
                getWeight() == (parcel.getWeight()) &&
                getWeightUnit().equals(parcel.getWeightUnit()) &&
                getCategory().equals(parcel.getCategory()) &&
                getCustomer().equals(parcel.getCustomer()) &&
                getDestination().equals(parcel.getDestination());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parcelId,
                customer.hashCode(),
                destination.hashCode(),
                category.hashCode(),
                weight,
                weightUnit.hashCode());
    }

}