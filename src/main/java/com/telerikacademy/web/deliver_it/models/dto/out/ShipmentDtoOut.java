package com.telerikacademy.web.deliver_it.models.dto.out;


import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.telerikacademy.web.deliver_it.utils.Formatters.dateToString;

public class ShipmentDtoOut implements IdentifyAble {

    @Positive
    int id;

    String shipmentStatus;

    @NotBlank
    String departureDate;

    @NotBlank
    String arrivalDate;

    @Positive
    int warehouseId;

    String warehouseCityName;

    @NotBlank
    List<Integer> parcelIds;

    public ShipmentDtoOut() {
    }

    public ShipmentDtoOut(@Positive int id, String shipmentStatus, @NotBlank Date departureDate,
                          @NotBlank Date arrivalDate, @Positive int warehouseId, String warehouseCityName,
                          @NotBlank List<Integer> parcelIds) {
        this.id = id;
        this.shipmentStatus = shipmentStatus;
        setDepartureDate(departureDate);
        setArrivalDate(arrivalDate);
        this.warehouseId = warehouseId;
        this.warehouseCityName = warehouseCityName;
        this.parcelIds = parcelIds;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseCityName() {
        return warehouseCityName;
    }

    public void setWarehouseCityName(String warehouseCityName) {
        this.warehouseCityName = warehouseCityName;
    }

    public List<Integer> getParcelIds() {
        return parcelIds;
    }

    public void setParcelIds(List<Integer> parcelIds) {
        this.parcelIds = parcelIds;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = dateToString(arrivalDate);
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = dateToString(departureDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipmentDtoOut that = (ShipmentDtoOut) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                shipmentStatus.equals(that.shipmentStatus) &&
                departureDate.equals(that.departureDate) &&
                arrivalDate.equals(that.arrivalDate) &&
                warehouseCityName.equals(that.warehouseCityName) &&
                parcelIds.equals(that.parcelIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                shipmentStatus,
                departureDate,
                arrivalDate,
                warehouseId,
                warehouseCityName,
                parcelIds);
    }
}
