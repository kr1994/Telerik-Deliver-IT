package com.telerikacademy.web.deliver_it.models;

import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "parcel_categories")
public class ParcelCategory implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parcel_category_id")
    private int id;

    @Column(name = "parcel_category")
    private String parcelCategory;

    public ParcelCategory() {
    }

    public ParcelCategory(int id, String parcelCategory) {
        this.id = id;
        this.parcelCategory = parcelCategory;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getParcelCategory() {
        return parcelCategory;
    }

    public void setParcelCategory(String parcelCategory) {
        this.parcelCategory = parcelCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ParcelCategory pc = (ParcelCategory) o;
        return getParcelCategory().equals(pc.getParcelCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parcelCategory);
    }

    @Override
    public String toString(){
        return String.format("Category id: %d [%s]", getId(),getParcelCategory());
    }
}
