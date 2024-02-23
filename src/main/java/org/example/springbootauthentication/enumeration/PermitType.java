package org.example.springbootauthentication.enumeration;

import lombok.Getter;

/**
 * 권한 허용 타입
 */
@Getter
public enum PermitType {

    HIERARCHY("hierarchy", "계층형 접근"),
    PERMIT_ALL("all", "모두 허용"),
    AUTHENTICATED("authenticated", "인증된 사용자"),
    ANONYMOUS("anonymous", "익명 사용자");

    private String permitType;
    private String text;

    PermitType(String permitType, String text) {
        this.permitType = permitType;
        this.text = text;
    }
}// PermitType
