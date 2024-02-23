package org.example.springbootauthentication.service;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Resource;
import org.example.springbootauthentication.domain.Role;
import org.example.springbootauthentication.dto.ResourceDTO;
import org.example.springbootauthentication.dto.RoleDTO;
import org.example.springbootauthentication.repository.ResourceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
class ResourceServiceTest {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceService resourceService;

    @Test
    @DisplayName("getAllResourcesWithRoles() : 모든 자원과 각 자원에 허용된 권한 조회에 성공한다.")
    void getAllResourcesWithRoles() {
        // given
        List<Resource> allWithRole = resourceRepository.findAllWithRole();

        // when
        List<ResourceDTO> allResourcesWithRoles = resourceService.getAllResourcesWithRoles();

        // then
        assertThat(allWithRole.size()).isEqualTo(allResourcesWithRoles.size());

        for (int i = 0; i < allResourcesWithRoles.size(); i++) {
            Resource    resource    = allWithRole.get(i);
            ResourceDTO resourceDTO = allResourcesWithRoles.get(i);

            assertThat(resource.getUrl()).isEqualTo(resourceDTO.getUrl());
            assertThat(resource.getMethod()).isEqualTo(resourceDTO.getMethod());

            if(resource.getResourceRoles().size() == 0) continue;

            for (int j = 0; j < resource.getResourceRoles().size(); j++) {
                Role    role    = resource.getResourceRoles().get(j).getRole();
                RoleDTO roleDTO = resourceDTO.getRoles().get(j);

                assertThat(role.getRole()).isEqualTo(roleDTO.getRole());
            }// for
        }// for

    }// getAllResourcesWithRoles

}// ResourceServiceTest