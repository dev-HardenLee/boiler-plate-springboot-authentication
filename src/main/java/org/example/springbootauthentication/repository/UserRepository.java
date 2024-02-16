package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u 
        FROM User u 
        JOIN FETCH u.role 
        WHERE 1=1 
        AND u.email = :username
    """)
    Optional<User> findByEmail(@Param("username") String username);
}
