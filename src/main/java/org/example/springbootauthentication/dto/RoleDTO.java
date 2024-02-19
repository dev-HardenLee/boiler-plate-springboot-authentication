package org.example.springbootauthentication.dto;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class RoleDTO {

    @Id
    private String role;

    private String text;

}
