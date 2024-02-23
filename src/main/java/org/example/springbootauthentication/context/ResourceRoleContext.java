package org.example.springbootauthentication.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.dto.ResourceDTO;
import org.example.springbootauthentication.enumeration.PermitType;
import org.example.springbootauthentication.manager.CustomRequestMatcherDelegatingAuthorizationManager;
import org.example.springbootauthentication.service.ResourceService;
import org.example.springbootauthentication.service.RoleService;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class ResourceRoleContext {

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final RoleHierarchyImpl roleHierarchy;

    private final CustomRequestMatcherDelegatingAuthorizationManager authorizationManager;

    public void initialize() {
        authorizationManager.getMappings().clear();

        String roleHierarchyStringRepresentation = roleService.roleHierarchyStringRepresentation();

        roleHierarchy.setHierarchy(roleHierarchyStringRepresentation);

        List<ResourceDTO> allResourcesWithRoles = resourceService.getAllResourcesWithRoles();

        for (ResourceDTO resourceDTO : allResourcesWithRoles) {
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resourceDTO.getUrl(), resourceDTO.getMethod());
            AuthorizationManager<RequestAuthorizationContext> manager = null;

            String permitType = resourceDTO.getPermitType();

            if(permitType.equals(PermitType.HIERARCHY.getPermitType())) {

                List<String> collect = resourceDTO.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());

                String[] roles = collect.toArray(new String[0]);

                AuthorityAuthorizationManager hierarchyManager = AuthorityAuthorizationManager.hasAnyAuthority(roles);

                hierarchyManager.setRoleHierarchy(roleHierarchy);

                manager = hierarchyManager;

            }else if(permitType.equals(PermitType.PERMIT_ALL.getPermitType())) {

                manager = (a, o) -> new AuthorizationDecision(true);

            }else if(permitType.equals(PermitType.AUTHENTICATED.getPermitType())) {

                manager = AuthenticatedAuthorizationManager.authenticated();

            }else if(permitType.equals(PermitType.ANONYMOUS.getPermitType())) {

                manager = AuthenticatedAuthorizationManager.anonymous();

            }// if-else

            RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> entry = new RequestMatcherEntry<>(requestMatcher, manager);

            authorizationManager.getMappings().add(entry);
        }// for
    }// initialize

}// ResourceRoleInitializeManager
