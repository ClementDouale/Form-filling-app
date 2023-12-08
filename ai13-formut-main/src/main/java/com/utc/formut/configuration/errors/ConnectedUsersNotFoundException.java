package com.utc.formut.configuration.errors;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ConnectedUsersNotFoundException extends RuntimeException {
    public ConnectedUsersNotFoundException() {super("Connected users not found.");}
}
