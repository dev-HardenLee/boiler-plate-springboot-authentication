package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("""
        SELECT role
        FROM Role role
        LEFT OUTER JOIN FETCH role.parentRole parentRole
        LEFT OUTER JOIN FETCH role.childRole childRole
    """)
    List<Role> findAllWithParentAndChildren();

    @Query("""
        SELECT role
        FROM Role role
        LEFT OUTER JOIN FETCH role.parentRole parentRole
        LEFT OUTER JOIN FETCH role.childRole child 
        WHERE 1=1
        AND role.role = :role
    """)
    Optional<Role> findByIdWithParentAndChildren(@Param("role") String role);

}
