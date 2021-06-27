package com.telerikacademy.web.deliver_it.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public abstract class UserBaseDto {

    @NotBlank(message = "First name can't be null or empty")
    private String firstName;

    @NotBlank(message = "Last name can't be null or empty")
    private String lastName;

    @NotBlank(message = "Email can't be null or empty")
    @Email(message = "It doesn't look like email.")
    private String email;

    public UserBaseDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
