package com.telerikacademy.web.deliver_it.models.dto;

import javax.validation.constraints.Positive;

public class ParcelDto {

    @Positive
    int customerId;

    @Positive
    int warehouseId;

    @Positive
    int parcelCategoryId;

    @Positive
    double weight;

    @Positive
    int weightUnitsId;

    @Positive
    int shipmentId;

    public ParcelDto() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getParcelCategoryId() {
        return parcelCategoryId;
    }

    public void setParcelCategoryId(int parcelCategoryId) {
        this.parcelCategoryId = parcelCategoryId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getWeightUnitsId() {
        return weightUnitsId;
    }

    public void setWeightUnitsId(int weightUnitsId) {
        this.weightUnitsId = weightUnitsId;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }
}
