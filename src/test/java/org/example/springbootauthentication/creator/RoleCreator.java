package org.example.springbootauthentication.creator;

import org.example.springbootauthentication.domain.Role;

public class RoleCreator {

    public static Role create(String role, String text) {
        return Role.builder()
                .role(role)
                .text(text)
                .build();
    }// creat

}// RoleCreator
