package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootauthentication.dto.LoginRequestDTO;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public LoginProcessingFilter(String url, HttpMethod httpMethod) {
        super(new AntPathRequestMatcher(url, httpMethod.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequestDTO requestDTO = objectMapper.readValue(request.getReader(), LoginRequestDTO.class);

        String username = requestDTO.getUsername() == null ? "" : requestDTO.getUsername();
        String password = requestDTO.getPassword() == null ? "" : requestDTO.getPassword();

        UsernamePasswordAuthenticationToken authRequestToken = new UsernamePasswordAuthenticationToken(username, password);

        authRequestToken.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(authRequestToken);
    }



}// LoginProcessingFilter












