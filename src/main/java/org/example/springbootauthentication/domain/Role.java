package org.example.springbootauthentication.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @Column(name = "ROLE")
    private String role;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ROLE")
    private Role parentRole;

    @OneToMany(mappedBy = "parentRole")
    private List<Role> childRole = new ArrayList<>();

    @Builder
    public Role(String role, String text, Role parentRole) {
        this.role = role;
        this.text = text;
        this.parentRole = parentRole;
    }

}
