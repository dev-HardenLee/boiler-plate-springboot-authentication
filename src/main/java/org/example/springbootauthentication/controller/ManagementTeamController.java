package org.example.springbootauthentication.controller;

import org.example.springbootauthentication.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagementTeamController {

    @GetMapping("/api/management-team")
    public ResponseEntity<ResponseDTO> managementTeam() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("Management Team 소속 권한만 접근할 수 있습니다.");

        return ResponseEntity.ok(responseDTO);
    }// managementTeam

}// ManagementTeamController
