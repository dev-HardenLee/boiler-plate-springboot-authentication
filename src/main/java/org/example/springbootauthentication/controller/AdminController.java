package org.example.springbootauthentication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.dto.ResponseDTO;
import org.example.springbootauthentication.dto.RoleDTO;
import org.example.springbootauthentication.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final RoleService roleService;

    @GetMapping("/api/admin")
    public ResponseEntity<ResponseDTO> permitAdmin() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage("ROLE_ADMIN 권한이 접근 가능합니다.");

        return ResponseEntity.ok(responseDTO);
    }// permitAdmin

    @PostMapping("/api/admin")
    public ResponseEntity<ResponseDTO> addRole(@RequestBody RoleDTO.PostRoleDTO postRoleDTO) throws Exception {
        roleService.addRole(postRoleDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("success"));
    }// addRole

}// AdminController











