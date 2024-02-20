package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {


}
