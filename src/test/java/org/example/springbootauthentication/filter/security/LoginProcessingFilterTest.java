package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.RefreshToken;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.LoginRequestDTO;
import org.example.springbootauthentication.repository.RefreshTokenRedisRepository;
import org.example.springbootauthentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class LoginProcessingFilterTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        objectMapper = new ObjectMapper();

        refreshTokenRedisRepository.deleteAll();
    }// beforeEach

    @Test
    @DisplayName("/api/login : 인증 성공 후 RefreshToken을 Redis에 저장하고 AccessToken과 RefreshToken을 응답 데이터으로 준다.")
    void login() throws Exception {
        // given
        String url = "/api/login";

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("harden@naver.com", "1234");

        String requestBody = objectMapper.writeValueAsString(loginRequestDTO);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());

        User         user         = userRepository.findByEmail(loginRequestDTO.getUsername()).get();
        RefreshToken refreshToken = refreshTokenRedisRepository.findById(user.getId()).orElse(null);

        assertThat(refreshToken).isNotNull();

        log.info(refreshToken);
    }// login

    @Test
    @DisplayName("/api/login : 없는 계정을 조회시에 401 에러가 발생한다.")
    void usernameNotFoundExceptionTest() throws Exception {
        // given
        String url = "/api/login";

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("notExistUser@naver.com", "1234");

        String requestBody = objectMapper.writeValueAsString(loginRequestDTO);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("해당 계정을 찾을 수가 없습니다."))
                .andDo(print());
    }// usernameNotFoundExceptionTest

    @Test
    @DisplayName("/api/login : 비밀번호가 틀리면 401 에러가 발생한다")
    void badCredentialsExceptionTest() throws Exception {
        // given
        String url = "/api/login";

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("harden@naver.com", "nonCorrectPassword");

        String requestBody = objectMapper.writeValueAsString(loginRequestDTO);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."))
                .andDo(print());
    }// usernameNotFoundExceptionTest

}