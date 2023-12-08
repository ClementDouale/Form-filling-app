package com.utc.formut.web.dto;

public class UserEditionDTO {
    Long id;
    String active;

    public UserEditionDTO(Long id, String active) {
        this.id = id;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
