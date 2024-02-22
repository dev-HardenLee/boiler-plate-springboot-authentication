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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class AdminControllerTest {

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
    @DisplayName("/api/admin : ROLE_ADMIN 권한을 가진 사용자의 접근을 허용한다.")
    @WithMockUser(username = "harden@naver.com", roles = {"ADMIN"})
    void admin() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/admin")
        );

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print());
    }// admin

    @Test
    @DisplayName("/api/admin : ROLE_USER 권한을 가진 사용자의 접근을 거부한다.")
    @WithMockUser(username = "harden@naver.com", roles = {"UESR"})
    void user() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/admin")
        );

        // then
        perform
                .andExpect(status().isForbidden())
                .andDo(print());
    }// user

    @Test
    @DisplayName("/api/admin : ROLE_IT_TEAM_CAPTAIN 권한을 가진 사용자의 접근을 거부한다.")
    @WithMockUser(username = "harden@naver.com", roles = {"IT_TEAM_CAPTAIN"})
    void ItCaptain() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/admin")
        );

        // then
        perform
                .andExpect(status().isForbidden())
                .andDo(print());
    }// ItCaptain

    @Test
    @DisplayName("/api/admin : ROLE_MANAGEMENT_TEAM_CAPTAIN 권한을 가진 사용자의 접근을 거부한다.")
    @WithMockUser(username = "harden@naver.com", roles = {"MANAGEMENT_TEAM_CAPTAIN"})
    void managementCaptain() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/admin")
        );

        // then
        perform
                .andExpect(status().isForbidden())
                .andDo(print());
    }// managementCaptain

}// AdminControllerTest