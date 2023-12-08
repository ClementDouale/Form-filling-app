package com.utc.formut.web.dto;

public class RegistrationConfirmationDTO {
    private String message;
    private String password;

    public RegistrationConfirmationDTO(String message, String password) {
        this.message = message;
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
