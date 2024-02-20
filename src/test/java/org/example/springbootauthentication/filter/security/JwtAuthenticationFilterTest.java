package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.LogoutToken;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.LogoutTokenRedisRepository;
import org.example.springbootauthentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogoutTokenRedisRepository logoutTokenRedisRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        objectMapper = new ObjectMapper();

        logoutTokenRedisRepository.deleteAll();
    }// beforeEach

    @Test
    @DisplayName("JWT 인증을 완료하여 자원 접근에 성공한다.")
    void jwtAuthenticationTest() throws Exception {
        // given
        User    user    = userRepository.findById(1L).get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken = jwtProvider.generateToken(Duration.ofSeconds(10L), userDTO);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/home")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(print());

    }// jwtAuthenticationTest

    @Test
    @DisplayName("로그아웃된 JWT로 접근시에 인증에 실패한다.")
    void jwtLogoutTest() throws Exception {
        // given
        User    user    = userRepository.findById(1L).get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken = jwtProvider.generateToken(Duration.ofSeconds(10L), userDTO);

        LogoutToken redisLogoutToken = LogoutToken.builder().id(accessToken).expiration(10L).build();

        logoutTokenRedisRepository.save(redisLogoutToken);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/home")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        );

        // then
        result
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }// jwtLogoutTest

    @Test
    @DisplayName("유효기간이 지난 JWT Token 으로 자원 접근 시 예외를 발생한다")
    void jwtExpireTest() throws Exception {
        // given
        User    user    = userRepository.findById(1L).get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken = jwtProvider.generateToken(Duration.ofSeconds(0), userDTO);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/home")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        );

        // then
        result
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// jwtExpireTest

}// JwtAuthenticationFilterTest