package org.example.springbootauthentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootauthentication.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    public void failHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");

        ResponseDTO responseDTO = new ResponseDTO();

        if(exception instanceof ExpiredJwtException e) {
            responseDTO.setMessage("토큰이 만료되었습니다.");
        } else {
            responseDTO.setMessage(exception.getMessage());
        }// if-else

        objectMapper.writeValue(response.getWriter(), responseDTO);
    }// failHandle

}// JwtAuthenticationFailureHandler














