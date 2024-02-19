package org.example.springbootauthentication.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("generateToken() : 토큰 생성에 성공한다.")
    void generateToken() {
        User user = userRepository.findById(1L).get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken = jwtProvider.generateToken(Duration.ofHours(1), userDTO);
        String refreshToken = jwtProvider.generateToken(Duration.ofDays(30), userDTO);

        assertThatCode(() -> jwtProvider.validToken(accessToken)).doesNotThrowAnyException();
        assertThatCode(() -> jwtProvider.validToken(refreshToken)).doesNotThrowAnyException();
    }// generateTokenTest

    @Test
    @DisplayName("validToken() : 유효기간이 지난 토큰은 토큰만료 예외를 발생시킨다.")
    void validToken() throws InterruptedException {
        User user = userRepository.findById(1L).get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken = jwtProvider.generateToken(Duration.ofSeconds(1L), userDTO);
        String refreshToken = jwtProvider.generateToken(Duration.ofSeconds(1L), userDTO);

        Thread.sleep(1500);

        assertThatThrownBy(() -> jwtProvider.validToken(accessToken)).isInstanceOf(ExpiredJwtException.class);
        assertThatThrownBy(() -> jwtProvider.validToken(refreshToken)).isInstanceOf(ExpiredJwtException.class);
    }// expiredTest

}// JwtProviderTest