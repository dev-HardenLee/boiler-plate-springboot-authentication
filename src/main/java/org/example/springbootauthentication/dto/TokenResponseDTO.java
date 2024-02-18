package org.example.springbootauthentication.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String accessToken;

    private String refreshToken;

    public TokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}// TokenResponseDTO
