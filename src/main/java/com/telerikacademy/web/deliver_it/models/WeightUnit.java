package com.telerikacademy.web.deliver_it.models;


import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "weight_units")
public class WeightUnit implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_unit_id")
    private int id;

    @Column(name = "weight_unit")
    private String units;

    @Column(name = "factor")
    private double factor;

    public WeightUnit() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int unitsId) {
        this.id = unitsId;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        WeightUnit wu = (WeightUnit) o;
        return getUnits().equals(wu.getUnits()) &&
                getFactor() == wu.getFactor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(units, factor);
    }

    @Override
    public String toString() {
        return String.format("[%s] weight unit id: %d", getUnits(), getId());
    }

}
