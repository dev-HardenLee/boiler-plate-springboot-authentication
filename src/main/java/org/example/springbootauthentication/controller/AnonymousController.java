package org.example.springbootauthentication.controller;

import org.example.springbootauthentication.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnonymousController {

    @GetMapping("/api/anonymous")
    public ResponseEntity<ResponseDTO> anonymous() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("익명 사용자만 접근 가능합니다.");

        return ResponseEntity.ok(responseDTO);
    }// anonymous

}// AnonymousController
