package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.RefreshToken;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.TokenRequestDTO;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.RefreshTokenRedisRepository;
import org.example.springbootauthentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.Duration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class JwtRefreshTokenFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .build();

        objectMapper = new ObjectMapper();

        refreshTokenRedisRepository.deleteAll();
    }// beforeEach

    @Test
    @DisplayName("/api/refresh-token : refreshToken을 이용하여 검증 후 새로운 accessToken발급에 성공한다.")
    void refreshTokenTest() throws Exception {
        // given
        User user = userRepository.findById(1L).orElse(null);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken  = jwtProvider.generateToken(Duration.ofSeconds(0L ), userDTO);
        String refreshToken = jwtProvider.generateToken(Duration.ofSeconds(30L), userDTO);

        RefreshToken redisRefreshToken = RefreshToken.builder().id(userDTO.getId()).refreshToken(refreshToken).expiration(30L).build();

        refreshTokenRedisRepository.save(redisRefreshToken);

        TokenRequestDTO requestDTO = new TokenRequestDTO(accessToken, refreshToken);

        String requestBody = objectMapper.writeValueAsString(requestDTO);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .characterEncoding("utf-8")
        );

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());

    }// refreshTokenTest

    @Test
    @DisplayName("/api/refresh-token : refreshToken이 조회가 되지 않으면 인증에 실패한다.")
    void refreshTokenNotExist() throws Exception {
        // given
        User user = userRepository.findById(1L).orElse(null);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken      = jwtProvider.generateToken(Duration.ofSeconds(0L ), userDTO);
        String refreshToken     = jwtProvider.generateToken(Duration.ofSeconds(30L), userDTO);

        RefreshToken redisRefreshToken = RefreshToken.builder().id(userDTO.getId()).refreshToken(refreshToken).expiration(30L).build();

        refreshTokenRedisRepository.save(redisRefreshToken);
        refreshTokenRedisRepository.delete(redisRefreshToken);

        TokenRequestDTO requestDTO = new TokenRequestDTO(accessToken, refreshToken);

        String requestBody = objectMapper.writeValueAsString(requestDTO);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .characterEncoding("utf-8")
        );

        // then
        result
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());

    }// refreshTokenNotExist

    @Test
    @DisplayName("/api/refresh-token : refreshToken이 저장된 값과 다르면 새로운 accessToken 발급에 실패한다.")
    void refreshTokenNotEqualsTest() throws Exception {
        // given
        User user = userRepository.findById(1L).orElse(null);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken      = jwtProvider.generateToken(Duration.ofSeconds(0L ), userDTO);
        String refreshToken     = jwtProvider.generateToken(Duration.ofSeconds(30L), userDTO);
        String fakeRefreshToken = jwtProvider.generateToken(Duration.ofSeconds(35L), userDTO);

        RefreshToken redisRefreshToken = RefreshToken.builder().id(userDTO.getId()).refreshToken(refreshToken).expiration(30L).build();

        refreshTokenRedisRepository.save(redisRefreshToken);

        TokenRequestDTO requestDTO = new TokenRequestDTO(accessToken, fakeRefreshToken);

        String requestBody = objectMapper.writeValueAsString(requestDTO);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .characterEncoding("utf-8")
        );

        // then
        result
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());

    }// refreshTokenTest

}// JwtRefreshTokenFilterTest