package com.telerikacademy.web.deliver_it.models;

import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class Country implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private int countryId;

    @Column(name = "country")
    private String countryName;

    public Country() {
    }

    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    @Override
    public int getId() {
        return countryId;
    }

    @Override
    public void setId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String country) {
        this.countryName = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return getCountryName().equals(country.getCountryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryName);
    }
}
