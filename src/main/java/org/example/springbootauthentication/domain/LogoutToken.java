package org.example.springbootauthentication.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "logout")
@ToString
public class LogoutToken {

    @Id
    private String id;

    @TimeToLive
    private Long expiration;

    @Builder
    public LogoutToken(String id, Long expiration) {
        this.id = id;
        this.expiration = expiration;
    }
}// LogoutToken
