package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.LogoutToken;
import org.example.springbootauthentication.dto.ResponseDTO;
import org.example.springbootauthentication.handler.JwtAuthenticationFailureHandler;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.LogoutTokenRedisRepository;
import org.example.springbootauthentication.repository.RefreshTokenRedisRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

import static org.example.springbootauthentication.filter.security.JwtAuthenticationFilter.HEADER_AUTHORIZATION;
import static org.example.springbootauthentication.provider.JwtProvider.getAccessToken;

@RequiredArgsConstructor
public class LogoutProcessingFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;

    private final JwtProvider jwtProvider;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final LogoutTokenRedisRepository logoutTokenRedisRepository;

    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);

            return;
        }// if

        String headerAuthorization = request.getHeader(HEADER_AUTHORIZATION);
        String accessToken = getAccessToken(headerAuthorization);

        try {
            jwtProvider.validToken(accessToken);
        } catch (ExpiredJwtException e) {
            jwtAuthenticationFailureHandler.failHandler(request, response, e);

            return;
        } catch (Exception e) {
            jwtAuthenticationFailureHandler.failHandler(request, response, e);

            return;
        }// try-catch

        Long userId = jwtProvider.getPrivateClaim(accessToken);

        refreshTokenRedisRepository.deleteById(userId);

        Long restSeconds = (jwtProvider.getExpiration(accessToken).getTime() - (new Date().getTime())) / 1000;

        LogoutToken logoutToken = LogoutToken.builder()
                .id(accessToken)
                .expiration(restSeconds)
                .build();

        logoutTokenRedisRepository.save(logoutToken);

        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("로그아웃 되었습니다.");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        
        objectMapper.writeValue(response.getWriter(), responseDTO);
    }// doFilterInternal
}// LogoutProcessingFilter
