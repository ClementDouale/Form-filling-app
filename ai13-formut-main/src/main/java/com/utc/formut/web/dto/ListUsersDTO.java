package com.utc.formut.web.dto;

import java.util.ArrayList;

public class ListUsersDTO {
    private ArrayList<UserDTO> listUserDTO = new ArrayList<>();

    public ListUsersDTO() {}

    public ListUsersDTO(ArrayList<UserDTO> listUserDTO) {
        this.listUserDTO = listUserDTO;
    }

    public ArrayList<UserDTO> getListUserDTO() {
        return listUserDTO;
    }

    public void setListUserDTO(ArrayList<UserDTO> listUserDTO) {
        this.listUserDTO = listUserDTO;
    }
}
