package org.example.springbootauthentication.service;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Role;
import org.example.springbootauthentication.dto.RoleDTO;
import org.example.springbootauthentication.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("roleHierarchySpringRepresentation() : 계층형 권한관계 문자열 표현식 완성에 성공한다.")
    void roleHierarchySpringRepresentation() {
        // given
        String expectedRepresentation = """
                ROLE_ADMIN > ROLE_USER
                ROLE_ADMIN > ROLE_IT_TEAM_CAPTAIN
                ROLE_IT_TEAM_CAPTAIN > ROLE_IT_TEAM_DEVELOPER
                ROLE_IT_TEAM_CAPTAIN > ROLE_IT_TEAM_PLANNER
                ROLE_ADMIN > ROLE_MANAGEMENT_TEAM_CAPTAIN
                ROLE_MANAGEMENT_TEAM_CAPTAIN > ROLE_MANAGEMENT_TEAM_PERSONAL
                ROLE_MANAGEMENT_TEAM_CAPTAIN > ROLE_MANAGEMENT_TEAM_AFFAIRS
                """;

        // when
        String roleHierarchyStringRepresentation = roleService.roleHierarchyStringRepresentation();

        // then
        assertThat(roleHierarchyStringRepresentation).isEqualTo(expectedRepresentation);
    }// roleHierarchySpringRepresentation

    @Test
    @DisplayName("addRole() : 권한 추가와 부모 권한과의 연관관계 매핑에 성공한다.")
    void addRole() throws Exception {
        // given
        Role roleAdmin = roleRepository.findById("ROLE_ADMIN").orElse(null);

        RoleDTO.PostRoleDTO postRoleDTO = RoleDTO.PostRoleDTO.builder().role("ROLE_TEST").text("테스트 권한").parentRole(roleAdmin.getRole()).build();

        // when
        roleService.addRole(postRoleDTO);

        Role newRole = roleRepository.findByIdWithParentAndChildren(postRoleDTO.getRole()).orElse(null);

        // then
        assertThat(newRole.getRole()).isEqualTo(postRoleDTO.getRole());
        assertThat(newRole.getParentRole().getRole()).isEqualTo(roleAdmin.getRole());

    }// addRole

}// RoleServiceTest
















