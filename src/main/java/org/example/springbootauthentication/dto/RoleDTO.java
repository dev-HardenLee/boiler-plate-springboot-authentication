package org.example.springbootauthentication.dto;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RoleDTO {

    @Id
    private String role;

    private String text;

    @Data
    @NoArgsConstructor
    public static class PostRoleDTO {

        private String parentRole;

        private String role;

        private String text;

        @Builder
        public PostRoleDTO(String parentRole, String role, String text) {
            this.parentRole = parentRole;
            this.role = role;
            this.text = text;
        }

    }// PostRoleDTO

}
