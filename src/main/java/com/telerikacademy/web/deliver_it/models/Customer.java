package com.telerikacademy.web.deliver_it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "customer_id")
public class Customer extends User {

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address delivery;

    public Customer() {
    }

    public Address getDelivery() {
        return delivery;
    }

    public void setDelivery(Address delivery) {
        this.delivery = delivery;
    }

    public int getAddressId(){
        return getDelivery().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return getDelivery().equals(customer.getDelivery()) &&
                super.equals(customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), delivery.hashCode());
    }
}
