package org.example.springbootauthentication.controller;

import org.example.springbootauthentication.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/api/auth")
    public ResponseEntity<ResponseDTO> auth() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("인증된 사용자만 접근 가능합니다.");

        return ResponseEntity.ok(responseDTO);
    }// auth

}// AuthController
