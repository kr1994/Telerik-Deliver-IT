package com.telerikacademy.web.deliver_it.models;


import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class SecurityCredentials implements IdentifyAble {

    @Positive
    private int id;

    @NotBlank
    private String name;

    private String password;

    public SecurityCredentials() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityCredentials that = (SecurityCredentials) o;
        return id == that.id &&
                name.equals(that.name) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }
}
