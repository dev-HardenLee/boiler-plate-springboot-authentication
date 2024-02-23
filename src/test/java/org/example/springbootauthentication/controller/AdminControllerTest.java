package org.example.springbootauthentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.Role;
import org.example.springbootauthentication.dto.RoleDTO;
import org.example.springbootauthentication.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .build();

        objectMapper = new ObjectMapper();
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

    @Test
    @DisplayName("/api/admin : 권한 추가에 성공한다.")
    @WithMockUser(username = "harden@naver.com", roles = {"ADMIN"})
    void addRole() throws Exception {
        // given
        Role roleAdmin = roleRepository.findById("ROLE_ADMIN").orElse(null);

        RoleDTO.PostRoleDTO postRoleDTO = RoleDTO.PostRoleDTO.builder().role("ROLE_TEST").text("테스트 권한").parentRole(roleAdmin.getRole()).build();

        String requestBody = objectMapper.writeValueAsString(postRoleDTO);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/admin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        );

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());

    }// addRole

    @Test
    @DisplayName("/api/admin : ADMIN 권한이 아닐시에 추가에 실패한다.")
    @WithMockUser(username = "harden@naver.com", roles = {"USER"})
    void addRole_access_denied() throws Exception {
        // given
        Role roleAdmin = roleRepository.findById("ROLE_ADMIN").orElse(null);

        RoleDTO.PostRoleDTO postRoleDTO = RoleDTO.PostRoleDTO.builder().role("ROLE_TEST").text("테스트 권한").parentRole(roleAdmin.getRole()).build();

        String requestBody = objectMapper.writeValueAsString(postRoleDTO);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/admin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        );

        // then
        result
                .andExpect(status().isForbidden())
                .andDo(print());

    }// addRole_access_denied

}// AdminControllerTest








