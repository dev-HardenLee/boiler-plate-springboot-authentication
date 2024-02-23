package org.example.springbootauthentication.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResourceDTO {

    private Long id;

    private String url;

    private String method;

    private String permitType; // hierarchy: 계층형 접근 허용, all: 모두 접근 혀용, authenticated: 인증된 사용자 접근 허용, anonymous: 익명 사용자 접근 허용

    private List<RoleDTO> roles = new ArrayList<>();

}
