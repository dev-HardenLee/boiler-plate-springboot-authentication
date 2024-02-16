package org.example.springbootauthentication.dto;

public class TokenResponseDTO {

    private String accessToken;

    private String refreshToken;

    public TokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}// TokenResponseDTO
