package org.example.springbootauthentication.repository;

import org.example.springbootauthentication.domain.LogoutToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutTokenRedisRepository extends CrudRepository<LogoutToken, String> {
}
