package org.example.springbootauthentication.controller;

import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }// beforeEach

    @Test
    @DisplayName("/api/auth : 인증한 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"ADMIN"})
    void admin() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/auth")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// admin

    @Test
    @DisplayName("/api/auth : 인증한 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"MANAGEMENT_TEAM_CAPTAIN"})
    void managementTeamCaptain() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/auth")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// managementTeamCaptain

    @Test
    @DisplayName("/api/auth : 인증한 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"IT_TEAM_CAPTAIN"})
    void itTeamCaptain() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/auth")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// itTeamCaptain

    @Test
    @DisplayName("/api/auth : 인증한 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"USER"})
    void user() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/auth")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// user

    @Test
    @DisplayName("/api/auth : 인증되지 않은 사용자의 접근을 거부한다.")
    @WithAnonymousUser
    void anonymous() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/auth")
        );

        // then
        result
                .andExpect(status().isForbidden())
                .andDo(print());
    }// anonymous
}