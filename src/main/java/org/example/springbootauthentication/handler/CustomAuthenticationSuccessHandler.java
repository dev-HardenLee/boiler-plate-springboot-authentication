package org.example.springbootauthentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.TokenResponseDTO;
import org.example.springbootauthentication.jwt.JwtProvider;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtProvider.generateToken(Duration.ofHours(1), user);
        String refreshToken = jwtProvider.generateToken(Duration.ofDays(30), user);

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(accessToken, refreshToken);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), tokenResponseDTO);
    }// onAuthenticationSuccess
}// CustomAuthenticationSuccessHandler
