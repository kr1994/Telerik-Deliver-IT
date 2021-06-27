package com.telerikacademy.web.deliver_it.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employees")
@PrimaryKeyJoinColumn(name = "employee_id")
public class Employee extends User {

    @JsonIgnore
    @Column(name = "password_hash")
    private String passHash;

    @JsonIgnore
    @Column(name = "salt")
    private double salt;

    public Employee() {
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public double getSalt() {
        return salt;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getPassHash().equals(employee.getPassHash()) &&
                getSalt() == employee.getSalt() &&
                super.equals(employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), passHash, salt);
    }

    @Override
    public String toString() {
        return String.format("Employee %s ", super.toString());
    }
}
