package org.example.springbootauthentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.RefreshToken;
import org.example.springbootauthentication.dto.TokenResponseDTO;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.RefreshTokenRedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final JwtProvider jwtProvider;

    @Value("${jwt.accessTokenExpireSeconds}")
    private Long accessTokenExpireSeconds;

    @Value("${jwt.refreshTokenExpireSeconds}")
    private Long refreshTokenExpireSeconds;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        String accessToken  = jwtProvider.generateToken(Duration.ofSeconds(accessTokenExpireSeconds ), userDTO);
        String refreshToken = jwtProvider.generateToken(Duration.ofSeconds(refreshTokenExpireSeconds), userDTO);

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(accessToken, refreshToken);

        RefreshToken redisRefreshToken = RefreshToken.builder().id(userDTO.getId()).refreshToken(refreshToken).expiration(refreshTokenExpireSeconds).build();

        refreshTokenRedisRepository.save(redisRefreshToken);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");

        objectMapper.writeValue(response.getWriter(), tokenResponseDTO);
    }// onAuthenticationSuccess
}// CustomAuthenticationSuccessHandler
