package com.telerikacademy.web.deliver_it.models.dto;

import java.util.List;

public class FilterByWeightDto {

    private Double minWeight;

    private String minUnit;

    private double minWeightFactor;

    private Double maxWeight;

    private String maxUnit;

    private double maxWeightFactor;

    private List<String> options;

    public FilterByWeightDto() {
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getMinUnit() {
        return minUnit;
    }

    public void setMinUnit(String minUnit) {
        this.minUnit = minUnit;
    }

    public String getMaxUnit() {
        return maxUnit;
    }

    public void setMaxUnit(String maxUnit) {
        this.maxUnit = maxUnit;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public double getMinWeightFactor() {
        return minWeightFactor;
    }

    public void setMinWeightFactor(double minWeightFactor) {
        this.minWeightFactor = minWeightFactor;
    }

    public double getMaxWeightFactor() {
        return maxWeightFactor;
    }

    public void setMaxWeightFactor(double maxWeightFactor) {
        this.maxWeightFactor = maxWeightFactor;
    }
}
