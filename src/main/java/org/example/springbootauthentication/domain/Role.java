package org.example.springbootauthentication.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"parentRole", "childRole"})
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
    public Role(String role, String text) {
        this.role = role;
        this.text = text;
    }

    public void makeRelationship(Role parentRole) {
        this.parentRole = parentRole;
        this.parentRole.getChildRole().add(this);
    }// makeRelationship

}
