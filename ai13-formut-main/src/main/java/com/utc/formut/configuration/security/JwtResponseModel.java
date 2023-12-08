package com.utc.formut.configuration.security;

import java.io.Serializable;

public class JwtResponseModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Long user_id;
    private final String token;

    public JwtResponseModel(String token, Long user_id) {
        this.token = token;
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public Long getUserID() {
        return user_id;
    }
}
