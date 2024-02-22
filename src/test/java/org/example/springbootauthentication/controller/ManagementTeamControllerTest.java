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
class ManagementTeamControllerTest {

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
    @DisplayName("/api/management-team : ROLE_ADMIN 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"ADMIN"})
    void admin() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/management-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// admin

    @Test
    @DisplayName("/api/management-team : ROLE_MANAGEMENT_TEAM_CAPTAIN 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"MANAGEMENT_TEAM_CAPTAIN"})
    void managementCaptain() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/management-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// managementCaptain

    @Test
    @DisplayName("/api/management-team : ROLE_MANAGEMENT_TEAM_PERSONAL 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"MANAGEMENT_TEAM_PERSONAL"})
    void managementPersonal() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/management-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// managementPersonal

    @Test
    @DisplayName("/api/management-team : ROLE_MANAGEMENT_TEAM_AFFAIRS 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"MANAGEMENT_TEAM_AFFAIRS"})
    void managementAffairs() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/management-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// managementAffairs

    @Test
    @DisplayName("/api/management-team : ROLE_MANAGEMENT_*와 연관 없는 권한을 가진 사용자의 접근이 거부된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"UESR"})
    void nonManagementTeam() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/management-team")
        );

        // then
        result
                .andExpect(status().isForbidden())
                .andDo(print());
    }// nonManagementTeam

    @Test
    @DisplayName("/api/management-team : 인증되지 않은 사용자의 접근을 거부한다.")
    @WithAnonymousUser
    void anonymous() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/management-team")
        );

        // then
        result
                .andExpect(status().isForbidden())
                .andDo(print());
    }// anonymous

}