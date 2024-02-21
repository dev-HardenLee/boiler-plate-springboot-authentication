package org.example.springbootauthentication.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESOURCE_ID")
    private Long id;

    private String url;

    private String method;

    private String permitType; // hierarchy: 계층형 접근 허용, all: 모두 접근 혀용, authenticated: 인증된 사용자 접근 허용, anonymous: 익명 사용자 접근 허용

}// Resource
