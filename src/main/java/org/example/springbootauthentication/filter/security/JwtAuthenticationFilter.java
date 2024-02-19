package org.example.springbootauthentication.filter.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.handler.JwtAuthenticationFailureHandler;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    private final ModelMapper modelMapper;

    private final static String HEADER_AUTHORIZATION = "Authorization";

    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerAuthorization = request.getHeader(HEADER_AUTHORIZATION);
        String accessToken         = getAccessToken(headerAuthorization);

        if(accessToken != null) {
            try {
                jwtProvider.validToken(accessToken);

                Long userId = jwtProvider.getPrivateClaim(accessToken);

                User user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

                UserDTO userDTO = modelMapper.map(user, UserDTO.class);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO, userDTO.getPassword(), List.of(new SimpleGrantedAuthority(userDTO.getRole().getRole())));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                jwtAuthenticationFailureHandler.failHandler(request, response, e);

                return;
            } catch (Exception e) {
                jwtAuthenticationFailureHandler.failHandler(request, response, e);

                return;
            }// try-catch
        }// if

        filterChain.doFilter(request, response);
    }// doFilterInternal

    private String getAccessToken(String headerAuthorization) {
        if(headerAuthorization != null && headerAuthorization.startsWith(TOKEN_PREFIX)) {
            return headerAuthorization.substring(TOKEN_PREFIX.length());
        }// if

        return null;
    }// getAccessToken

}// JwtAuthenticationFilter






























