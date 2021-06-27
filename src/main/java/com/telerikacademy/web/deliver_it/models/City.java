package com.telerikacademy.web.deliver_it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cities")
public class City implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private int cityId;

    @Column(name = "city")
    private String cityName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    public City() {
    }

    public City(int cityId, String cityName, Country country) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.country = country;
    }

    @Override
    public int getId() {
        return this.cityId;
    }

    @Override
    public void setId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String city) {
        this.cityName = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCountryName(){
        return getCountry().getCountryName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        City city = (City) o;
        return getCityName().equals(city.getCityName()) &&
                getCountry().equals(city.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country.hashCode());
    }
}
