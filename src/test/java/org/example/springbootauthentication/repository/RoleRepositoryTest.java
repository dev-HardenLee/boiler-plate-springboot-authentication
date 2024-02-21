package org.example.springbootauthentication.repository;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@Log4j2
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("findAll() : 전체 엔티티와 자식 및 부모 엔티티 조회에 성공한다.")
    void findAllWithChildren() {
        List<Role> roles = roleRepository.findAllWithChildren();

        for (Role role : roles) {
            log.info(role + " / parent :  " + role.getParentRole() + " / children : " + role.getChildRole());
        }
    }// findAllWithChildren

}// RoleRepositoryTest