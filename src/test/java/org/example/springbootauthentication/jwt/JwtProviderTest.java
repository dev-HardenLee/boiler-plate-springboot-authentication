package org.example.springbootauthentication.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Test
    void generateTest() {
        User user = userRepository.findById(1L).get();

        String accessToken = jwtProvider.generateToken(Duration.ofHours(1), user);
        String refreshToken = jwtProvider.generateToken(Duration.ofDays(30), user);

        assertThatCode(() -> jwtProvider.validToken(accessToken)).doesNotThrowAnyException();
        assertThatCode(() -> jwtProvider.validToken(refreshToken)).doesNotThrowAnyException();
    }// generateTokenTest

    @Test
    void expiredTest() throws InterruptedException {
        User user = userRepository.findById(1L).get();

        String accessToken = jwtProvider.generateToken(Duration.ofSeconds(1L), user);
        String refreshToken = jwtProvider.generateToken(Duration.ofSeconds(1L), user);

        Thread.sleep(1500);

        assertThatThrownBy(() -> jwtProvider.validToken(accessToken)).isInstanceOf(ExpiredJwtException.class);
        assertThatThrownBy(() -> jwtProvider.validToken(refreshToken)).isInstanceOf(ExpiredJwtException.class);
    }// expiredTest

}// JwtProviderTest