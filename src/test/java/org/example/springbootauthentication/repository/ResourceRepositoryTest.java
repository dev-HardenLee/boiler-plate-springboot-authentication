package org.example.springbootauthentication.repository;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Resource;
import org.example.springbootauthentication.domain.ResourceRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@Log4j2
class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    @DisplayName("findAllWithRole() : 모든 자원과 각 자원에 허가되는 권한 조회에 성공한다.")
    void findAllWithRole() {
        List<Resource> allWithRole = resourceRepository.findAllWithRole();

        for (Resource resource : allWithRole) {
            log.info(resource);
            if(resource.getResourceRoles().size() == 0) continue;

            for (ResourceRole resourceRole : resource.getResourceRoles()) {
                log.info("  " + resourceRole.getRole());
            }// for
        }// for
    }// findAllWithRole

}// ResourceRepositoryTest














