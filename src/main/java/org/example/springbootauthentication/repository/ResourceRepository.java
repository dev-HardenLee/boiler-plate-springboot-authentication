package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("""
        SELECT resource
        FROM Resource resource
        LEFT OUTER JOIN FETCH resource.resourceRoles resourceRoles
        LEFT OUTER JOIN FETCH resourceRoles.role role
    """)
    List<Resource> findAllWithRole();

}
