package com.telerikacademy.web.deliver_it.models;

import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "statuses")
public class ShipmentStatus implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    int id;

    @Column(name = "status")
    String status;

    public ShipmentStatus() {
    }

    public ShipmentStatus(int id, String status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ShipmentStatus status = (ShipmentStatus) o;
        return getStatus().equals(status.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString(){
        return String.format("Status id: %d [%s]",getId(),getStatus());
    }
}
