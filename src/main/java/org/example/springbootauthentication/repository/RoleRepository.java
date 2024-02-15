package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
