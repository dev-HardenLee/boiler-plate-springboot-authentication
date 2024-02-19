package org.example.springbootauthentication.controller;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.dto.ResponseDTO;
import org.example.springbootauthentication.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class HomeController {

    @GetMapping("/api/home")
    public ResponseEntity<ResponseDTO> home(@AuthenticationPrincipal UserDTO userDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage(userDTO.toString());

        return ResponseEntity.ok(responseDTO);
    }// home

}// HomeController
