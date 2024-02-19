package org.example.springbootauthentication.controller;

import org.example.springbootauthentication.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/api/admin")
    public ResponseEntity<ResponseDTO> permitAdmin() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("ROLE_ADMIN 권한이 접근 가능합니다.");

        return ResponseEntity.ok(responseDTO);
    }

}// AdminController
