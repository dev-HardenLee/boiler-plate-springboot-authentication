package org.example.springbootauthentication.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;

    public LoginRequestDTO(){}

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
