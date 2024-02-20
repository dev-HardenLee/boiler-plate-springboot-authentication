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
import org.example.springbootauthentication.repository.LogoutTokenRedisRepository;
import org.example.springbootauthentication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.example.springbootauthentication.provider.JwtProvider.getAccessToken;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private final LogoutTokenRedisRepository logoutTokenRedisRepository;

    private final JwtProvider jwtProvider;

    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    private final ModelMapper modelMapper;

    public final static String HEADER_AUTHORIZATION = "Authorization";

    public final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerAuthorization = request.getHeader(HEADER_AUTHORIZATION);
        String accessToken         = getAccessToken(headerAuthorization);

        if(accessToken != null) {
            try {
                if(logoutTokenRedisRepository.findById(accessToken).isPresent()) throw new Exception("로그아웃 처리된 토큰입니다.");

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

}// JwtAuthenticationFilter






























