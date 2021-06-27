package com.telerikacademy.web.deliver_it.models.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;


public class ShipmentDto {

    @NotNull(message = "Date can't be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    Date departureDate;

    @NotNull(message = "Date can't be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    Date arrivalDate;

    @Positive
    int shipmentStatusId;

    @Positive
    int warehouseId;

    public ShipmentDto() {
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getShipmentStatusId() {
        return shipmentStatusId;
    }

    public void setShipmentStatusId(int shipmentStatusId) {
        this.shipmentStatusId = shipmentStatusId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }
}
