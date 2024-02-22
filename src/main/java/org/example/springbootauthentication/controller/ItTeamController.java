package org.example.springbootauthentication.controller;

import org.example.springbootauthentication.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItTeamController {

    @GetMapping("/api/it-team")
    public ResponseEntity<ResponseDTO> itTeam() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("IT Team 소속 권한만 접근 가능합니다.");

        return ResponseEntity.ok(responseDTO);
    }// itTeam

}// ItTeamController
