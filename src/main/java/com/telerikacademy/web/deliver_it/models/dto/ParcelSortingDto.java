package com.telerikacademy.web.deliver_it.models.dto;

import javax.validation.constraints.NotNull;

public class ParcelSortingDto {

    private Integer customerId;

    private Integer warehouseId;

    private Double weight;

    private String weightUnit;

    private Integer parcelCategoryId;

    @NotNull
    private String weightSort;

    @NotNull
    private String arrivalDateSort;

    public ParcelSortingDto() {
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getParcelCategoryId() {
        return parcelCategoryId;
    }

    public void setParcelCategoryId(Integer parcelCategoryId) {
        this.parcelCategoryId = parcelCategoryId;
    }

    public String getWeightSort() {
        return weightSort;
    }

    public void setWeightSort(String weightSort) {
        this.weightSort = weightSort;
    }

    public String getArrivalDateSort() {
        return arrivalDateSort;
    }

    public void setArrivalDateSort(String arrivalDateSort) {
        this.arrivalDateSort = arrivalDateSort;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }
}
