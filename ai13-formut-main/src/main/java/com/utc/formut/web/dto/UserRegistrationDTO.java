package com.utc.formut.web.dto;

import java.util.Date;

public class UserRegistrationDTO {
    String name;
    String surname;
    String society;
    String email;
    String phone;
    String status = "enabled";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String company) {
        this.society = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
