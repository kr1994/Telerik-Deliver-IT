package com.telerikacademy.web.deliver_it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "addresses")
public class Address implements IdentifyAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;

    @Column(name = "street_line_1")
    private String street1;

    @Column(name = "street_line_2")
    private String street2;

    @Column(name = "post_code")
    private String postCode;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public Address() {
    }

    public Address(int addressId, String street1, String street2,
                   String postCode, City city) {
        this.addressId = addressId;
        this.street1 = street1;
        this.street2 = street2;
        this.postCode = postCode;
        this.city = city;
    }

    @Override
    public int getId() {
        return addressId;
    }

    @Override
    public void setId(int addressId) {
        this.addressId = addressId;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCityName() {
        return city.getCityName();
    }

    public String getCountryName() {
        return city.getCountry().getCountryName();
    }

    public String toString() {
        return this.postCode + ", " + this.street1 + " " + this.street2 + ", " +
                this.getCityName() + ", " + this.getCountryName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return getStreet1().equals(address.getStreet1()) &&
                getStreet2().equals(address.getStreet2()) &&
                getPostCode().equals(address.getPostCode()) &&
                getCity().equals(address.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                street1,
                street2,
                postCode,
                city.hashCode());
    }
}
