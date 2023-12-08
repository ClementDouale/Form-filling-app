package com.utc.formut.configuration.errors;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {super("User not found.");}
}
