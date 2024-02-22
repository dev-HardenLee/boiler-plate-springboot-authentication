package org.example.springbootauthentication.runner;

import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.service.RoleService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleHierarchyRunner implements ApplicationRunner {

    private final RoleService roleService;

    private final RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String roleHierarchyStringRepresentation = roleService.roleHierarchyStringRepresentation();

        roleHierarchy.setHierarchy(roleHierarchyStringRepresentation);
    }// run

}// RoleHierarchyRunner
