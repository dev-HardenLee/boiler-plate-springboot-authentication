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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class ItTeamControllerTest {

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
    @DisplayName("/api/it-team : ROLE_ADMIN 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"ADMIN"})
    void admin() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/it-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// admin

    @Test
    @DisplayName("/api/it-team : ROLE_IT_TEAM_CAPTAIN 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"IT_TEAM_CAPTAIN"})
    void itTeamCaptain() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/it-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// itTeamCaptain

    @Test
    @DisplayName("/api/it-team : ROLE_IT_TEAM_DEVELOPER 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"IT_TEAM_DEVELOPER"})
    void itTeamDeveloper() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/it-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// itTeamDeveloper

    @Test
    @DisplayName("/api/it-team : ROLE_IT_TEAM_PLANNER 권한을 가진 사용자의 접근이 허용된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"IT_TEAM_PLANNER"})
    void itTeamPlanner() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/it-team")
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
    }// itTeamPlanner

    @Test
    @DisplayName("/api/it-team : ROLE_IT_*와 연관 없는 권한을 가진 사용자의 접근이 거부된다.")
    @WithMockUser(username = "harden@naver.com", roles = {"UESR"})
    void nonItTeam() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/it-team")
        );

        // then
        result
                .andExpect(status().isForbidden())
                .andDo(print());
    }// nonItTeam

    @Test
    @DisplayName("/api/it-team : 인증되지 않은 사용자의 접근을 거부한다.")
    @WithAnonymousUser
    void anonymous() throws Exception {
        // given


        // when
        ResultActions result = mockMvc.perform(
                get("/api/it-team")
        );

        // then
        result
                .andExpect(status().isForbidden())
                .andDo(print());
    }// anonymous
}// ItTeamControllerTest