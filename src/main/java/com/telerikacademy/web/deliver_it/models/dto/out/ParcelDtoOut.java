package com.telerikacademy.web.deliver_it.models.dto.out;

import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.Objects;

import static com.telerikacademy.web.deliver_it.utils.Formatters.dateToString;

public class ParcelDtoOut implements IdentifyAble {

    @Positive
    int id;

    @Positive
    int customerId;

    String customerName;

    int shipmentWarehouseId;

    int parcelWarehouseId;

    String warehouseCityName;

    String categoryName;

    @Positive
    double weight;

    @NotBlank
    String weightUnits;

    private double weightFactor;

    @Positive
    int shipmentId;

    String arrivalDate;

    String shipmentStatus;

    public ParcelDtoOut() {
    }

    public ParcelDtoOut(@Positive int id, @Positive int customerId, String customerName, int shipmentWarehouseId,
                        String warehouseCityName, String categoryName, @Positive double weight,
                        @NotBlank String weightUnits, @Positive int shipmentId, Date arrivalDate,
                        String shipmentStatus, double weightFactor, int parcelWarehouseId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.shipmentWarehouseId = shipmentWarehouseId;
        this.warehouseCityName = warehouseCityName;
        this.categoryName = categoryName;
        this.weight = weight;
        this.weightUnits = weightUnits;
        this.shipmentId = shipmentId;
        setArrivalDate(arrivalDate);
        this.shipmentStatus = shipmentStatus;
        this.id = id;
        this.weightFactor = weightFactor;
        this.parcelWarehouseId = parcelWarehouseId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getShipmentWarehouseId() {
        return shipmentWarehouseId;
    }

    public void setShipmentWarehouseId(int shipmentWarehouseId) {
        this.shipmentWarehouseId = shipmentWarehouseId;
    }

    public String getWarehouseCityName() {
        return warehouseCityName;
    }

    public void setWarehouseCityName(String warehouseCityName) {
        this.warehouseCityName = warehouseCityName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightUnits() {
        return weightUnits;
    }

    public void setWeightUnits(String weightUnits) {
        this.weightUnits = weightUnits;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = dateToString(arrivalDate);
    }

    public double getWeightFactor() {
        return weightFactor;
    }

    public void setWeightFactor(double weightFactor) {
        this.weightFactor = weightFactor;
    }

    public int getParcelWarehouseId() {
        return parcelWarehouseId;
    }

    public void setParcelWarehouseId(int parcelWarehouseId) {
        this.parcelWarehouseId = parcelWarehouseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParcelDtoOut that = (ParcelDtoOut) o;
        return id == that.id &&
                customerId == that.customerId &&
                shipmentWarehouseId == that.shipmentWarehouseId
                && Double.compare(that.weight, weight) == 0 &&
                shipmentId == that.shipmentId &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(warehouseCityName, that.warehouseCityName) &&
                Objects.equals(categoryName, that.categoryName) &&
                weightUnits.equals(that.weightUnits) &&
                Objects.equals(arrivalDate, that.arrivalDate) &&
                Objects.equals(shipmentStatus, that.shipmentStatus) &&
                weightFactor == that.getWeightFactor() &&
                parcelWarehouseId == that.parcelWarehouseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, customerName, shipmentWarehouseId, warehouseCityName, categoryName,
                weight, weightUnits, shipmentId, arrivalDate, shipmentStatus, weightFactor, parcelWarehouseId);
    }
}
