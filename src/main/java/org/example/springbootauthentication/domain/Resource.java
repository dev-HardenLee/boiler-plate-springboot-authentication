package org.example.springbootauthentication.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"resourceRoles"})
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESOURCE_ID")
    private Long id;

    private String url;

    private String method;

    private String permitType; // hierarchy: 계층형 접근 허용, all: 모두 접근 혀용, authenticated: 인증된 사용자 접근 허용, anonymous: 익명 사용자 접근 허용

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resource")
    private List<ResourceRole> resourceRoles = new ArrayList<>();

    @Builder
    public Resource(Long id, String url, String method, String permitType) {
        this.id = id;
        this.url = url;
        this.method = method;
        this.permitType = permitType;
    }
}// Resource


















