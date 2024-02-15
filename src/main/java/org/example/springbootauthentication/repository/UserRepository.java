package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
