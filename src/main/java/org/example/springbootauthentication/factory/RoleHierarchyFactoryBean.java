package org.example.springbootauthentication.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.service.RoleService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@RequiredArgsConstructor
@Log4j2
public class RoleHierarchyFactoryBean implements FactoryBean<RoleHierarchyImpl> {

    private RoleHierarchyImpl roleHierarchy;

    private final RoleService roleService;

    @Override
    public RoleHierarchyImpl getObject() throws Exception {
        if(roleHierarchy != null) return roleHierarchy;

        String roleHierarchyStringRepresentation = roleService.roleHierarchyStringRepresentation();

        roleHierarchy = new RoleHierarchyImpl();

        roleHierarchy.setHierarchy(roleHierarchyStringRepresentation);

        return roleHierarchy;
    }// getObject

    @Override
    public Class<?> getObjectType() {
        return RoleHierarchyImpl.class;
    }


}// RoleHierarchyFactoryBean
