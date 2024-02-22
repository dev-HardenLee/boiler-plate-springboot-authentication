package org.example.springbootauthentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Role;
import org.example.springbootauthentication.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class RoleService {

    private final RoleRepository roleRepository;

    public String roleHierarchyStringRepresentation() throws NullPointerException {
        roleRepository.findAllWithChildren();

        Role roleAdmin = roleRepository.findById("ROLE_ADMIN").orElseThrow(() -> new NullPointerException("ROLE_ADMIN 권한이 없습니다."));

        StringBuilder sb = new StringBuilder();

        dfs(roleAdmin, sb);

        return sb.toString();
    }// roleHierarchyStringRepresentation

    private void dfs(Role parentRole, StringBuilder sb) {
        List<Role> childRole = parentRole.getChildRole();

        for (Role child : childRole) {
            sb.append(parentRole.getRole() + " > " + child.getRole() + "\n");

            dfs(child, sb);
        }// for
    }// dfs

}// RoleService
