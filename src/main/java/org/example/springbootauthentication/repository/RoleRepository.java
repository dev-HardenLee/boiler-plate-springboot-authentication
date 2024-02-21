package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("""
        SELECT role
        FROM Role role
        LEFT OUTER JOIN FETCH role.childRole
    """)
    List<Role> findAllWithChildren();

}
