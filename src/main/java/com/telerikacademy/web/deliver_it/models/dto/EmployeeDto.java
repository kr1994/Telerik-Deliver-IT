package com.telerikacademy.web.deliver_it.models.dto;

import javax.validation.constraints.NotBlank;

public class EmployeeDto extends UserBaseDto {

    @NotBlank(message = "Value can't be null or empty")
    private String password;

    public EmployeeDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
