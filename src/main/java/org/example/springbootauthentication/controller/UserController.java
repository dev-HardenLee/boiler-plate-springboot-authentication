package org.example.springbootauthentication.controller;

import org.example.springbootauthentication.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/user")
    public ResponseEntity<ResponseDTO> permitUser() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("ROLE_USER 상위 권한을 가진 사용자가 접근 가능합니다.");

        return ResponseEntity.ok(responseDTO);
    }

}
