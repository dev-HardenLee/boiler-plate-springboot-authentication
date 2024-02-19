package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.RefreshToken;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.TokenRequestDTO;
import org.example.springbootauthentication.dto.TokenResponseDTO;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.handler.JwtAuthenticationFailureHandler;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.RefreshTokenRedisRepository;
import org.example.springbootauthentication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

import static org.example.springbootauthentication.provider.JwtProvider.PRIVATE_CLAIM;

@RequiredArgsConstructor
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;

    private final JwtProvider jwtProvider;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Value("${jwt.accessTokenExpireSeconds}")
    private Long accessTokenExpireSeconds;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);

            return;
        }// if

        TokenRequestDTO tokenDTO = objectMapper.readValue(request.getReader(), TokenRequestDTO.class);

        Long userId = null;

        try {
            jwtProvider.validToken(tokenDTO.getAccessToken());
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().get(PRIVATE_CLAIM, Long.class);
        } catch (Exception e) {
            jwtAuthenticationFailureHandler.failHandler(request, response, e);

            return;
        }// try-catch

        try {
            jwtProvider.validToken(tokenDTO.getRefreshToken());
        } catch (ExpiredJwtException e) {
            jwtAuthenticationFailureHandler.failHandler(request, response, e);

            return;
        } catch (Exception e) {
            jwtAuthenticationFailureHandler.failHandler(request, response, e);

            return;
        }// try-catch

        RefreshToken refreshToken = refreshTokenRedisRepository.findById(userId).orElse(null);

        if(refreshToken == null) {
            jwtAuthenticationFailureHandler.failHandler(request, response, new Exception("Refresh Token이 존재하지 않습니다."));

            return;
        } else if(!refreshToken.getRefreshToken().equals(tokenDTO.getRefreshToken())) {
            jwtAuthenticationFailureHandler.failHandler(request, response, new Exception("Refresh Token이 일치하지 않습니다."));

            return;
        }// if

        User    user    = userRepository.findById(userId).orElse(null);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken = jwtProvider.generateToken(Duration.ofSeconds(accessTokenExpireSeconds), userDTO);

        TokenResponseDTO responseDTO = new TokenResponseDTO(accessToken);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");

        objectMapper.writeValue(response.getWriter(), responseDTO);
    }// doFilterInternal


}// JwtRefreshTokenFilter
