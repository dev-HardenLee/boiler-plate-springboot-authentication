package org.example.springbootauthentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        FailMessageResponseDTO responseDTO = new FailMessageResponseDTO();

        if(exception instanceof UsernameNotFoundException e) {
            responseDTO.setMessage("해당 계정을 찾을 수가 없습니다.");
        }else if(exception instanceof BadCredentialsException e) {
            responseDTO.setMessage("비밀번호가 일치하지 않습니다.");
        }else {
            responseDTO.setMessage(exception.getMessage());
        }// if-else

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        objectMapper.writeValue(response.getWriter(), responseDTO);
    }// onAuthenticationFailure

    @Data
    private static class FailMessageResponseDTO {
        private String message;
    }

}
