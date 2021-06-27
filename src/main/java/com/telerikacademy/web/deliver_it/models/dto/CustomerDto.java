package com.telerikacademy.web.deliver_it.models.dto;

import javax.validation.constraints.Positive;

public class CustomerDto extends UserBaseDto {

    @Positive
    private int addressId;

    public CustomerDto() {
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

}
