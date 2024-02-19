package org.example.springbootauthentication.repository;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.RefreshToken;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
class RefreshTokenRedisRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @BeforeEach
    void beforeEach() {
        refreshTokenRedisRepository.deleteAll();
    }// beforeEach

    @Test
    void saveTest() {
        // given
        User user = userRepository.findById(1L).get();

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String token = jwtProvider.generateToken(Duration.ofSeconds(10), userDTO);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .refreshToken(token)
                .expiration(10L)
                .build();

        refreshTokenRedisRepository.save(refreshToken);

        // when
        RefreshToken byId = refreshTokenRedisRepository.findById(refreshToken.getId()).orElse(null);

        // then
        assertThat(byId).isNotNull();
        assertThat((byId.getRefreshToken())).isEqualTo(refreshToken.getRefreshToken());
    }// saveTest

}// RefreshTokenRedisRepositoryTest