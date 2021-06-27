package com.telerikacademy.web.deliver_it.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class AddressDto {

    @NotBlank(message = "Address line1 can't be blank or empty")
    private String street1;

    private String street2;

    @NotBlank(message = "Post code can't be null or empty")
    private String postCode;

    @Positive(message = "City id mast be positive")
    private int cityId;


    public AddressDto() {
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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
}
