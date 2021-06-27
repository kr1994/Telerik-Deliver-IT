package com.telerikacademy.web.deliver_it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "shipments")
public class Shipment implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private int shipmentId;

    @Column(name = "departure_date")
    @Temporal(TemporalType.DATE)
    private Date departureDate;

    @Column(name = "arrival_date")
    @Temporal(TemporalType.DATE)
    private Date arrivalDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "status_id")
    private ShipmentStatus status;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id")
    private Set<Parcel> parcels;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse destination;

    public Shipment() {
    }

    @Override
    public int getId() {
        return shipmentId;
    }

    @Override
    public void setId(int shipmentId) {
        this.shipmentId = shipmentId;
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

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public Set<Parcel> getParcels() {
        return parcels;
    }

    @JsonIgnore
    public List<Parcel> getParcelsList() {
        return new ArrayList<>(parcels);
    }

    public void setParcels(Set<Parcel> parcels) {
        this.parcels = parcels;
    }

    public Warehouse getDestination() {
        return destination;
    }

    public void setDestination(Warehouse destination) {
        this.destination = destination;
    }

    public String getStatusInfo(){
        return getStatus().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return getDestination().equals(shipment.getDestination()) &&
                getStatus().equals(shipment.getStatus()) &&
                getDepartureDate().equals(shipment.getDepartureDate()) &&
                getArrivalDate().equals(shipment.getArrivalDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureDate,
                arrivalDate,
                status.hashCode(),
                destination.hashCode());
    }
}
