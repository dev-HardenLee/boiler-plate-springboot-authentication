package org.example.springbootauthentication.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refresh")
public class RefreshToken {

    @Id
    private Long id;

    private String refreshToken;

}// RefreshToken
