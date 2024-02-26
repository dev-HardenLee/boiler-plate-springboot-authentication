package org.example.springbootauthentication.repository;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.springbootauthentication.creator.RoleCreator.create;

@SpringBootTest
@Log4j2
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("findAll() : 전체 엔티티와 자식 및 부모 엔티티 조회에 성공한다.")
    void findAllWithChildren() {
        List<Role> roles = roleRepository.findAllWithParentAndChildren();

        for (Role role : roles) {
            log.info(role + " / parent :  " + role.getParentRole() + " / children : " + role.getChildRole());
        }
    }// findAllWithChildren

    @Test
    @DisplayName("addRole() : 권한 추가와 부모 권한과의 연관관계 매핑에 성공한다.")
    void addRole() {
        // given
        Role roleAdmin = roleRepository.findById("ROLE_ADMIN").orElse(null);
        Role roleTest  = create("ROLE_TEST", "테스트 권한");

        roleTest.makeRelationship(roleAdmin);

        roleRepository.save(roleTest);

        // when
        Role findRole = roleRepository.findByIdWithParentAndChildren(roleTest.getRole()).orElse(null);

        // then
        assertThat(roleTest.getRole()).isEqualTo(findRole.getRole());
        assertThat(findRole.getParentRole().getRole()).isEqualTo(roleAdmin.getRole());

    }// addRole

}// RoleRepositoryTest













