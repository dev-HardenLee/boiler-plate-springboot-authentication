package org.example.springbootauthentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Resource;
import org.example.springbootauthentication.domain.ResourceRole;
import org.example.springbootauthentication.dto.ResourceDTO;
import org.example.springbootauthentication.dto.RoleDTO;
import org.example.springbootauthentication.repository.ResourceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ResourceService {

    private final ResourceRepository resourceRepository;

    private final ModelMapper modelMapper;

    public List<ResourceDTO> getAllResourcesWithRoles() {
        List<Resource> allWithRole = resourceRepository.findAllWithRole();

        return allWithRole.stream().map(resource -> {
            ResourceDTO resourceDTO = modelMapper.map(resource, ResourceDTO.class);

            if(resource.getResourceRoles().size() == 0) return resourceDTO;

            for (ResourceRole resourceRole : resource.getResourceRoles()) {
                RoleDTO roleDTO = modelMapper.map(resourceRole.getRole(), RoleDTO.class);

                resourceDTO.getRoles().add(roleDTO);
            }// for

            return resourceDTO;
        }).collect(Collectors.toList());
    }// getAllResourcesWithRoles

}// ResourceService
