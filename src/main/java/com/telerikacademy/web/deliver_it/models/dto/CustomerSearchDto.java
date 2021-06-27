package com.telerikacademy.web.deliver_it.models.dto;

public class CustomerSearchDto extends UserBaseDto{

    String any;

    public CustomerSearchDto() {
    }

    public String getAny() {
        return any;
    }

    public void setAny(String any) {
        this.any = any;
    }
}
