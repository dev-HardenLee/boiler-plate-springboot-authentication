package org.example.springbootauthentication.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

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

}// RoleServiceTest
















