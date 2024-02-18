package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootauthentication.dto.LoginRequestDTO;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginProcessingFilterTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        objectMapper = new ObjectMapper();
    }// beforeEach

    @Test
    @DisplayName("/api/login : 인증 성공 후 토큰을 응답 데이터으로 준다.")
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