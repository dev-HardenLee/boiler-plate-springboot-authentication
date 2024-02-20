package org.example.springbootauthentication.filter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.domain.LogoutToken;
import org.example.springbootauthentication.domain.RefreshToken;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.repository.LogoutTokenRedisRepository;
import org.example.springbootauthentication.repository.RefreshTokenRedisRepository;
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
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
class LogoutProcessingFilterTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Autowired
    private LogoutTokenRedisRepository logoutTokenRedisRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        objectMapper = new ObjectMapper();

        refreshTokenRedisRepository.deleteAll();
    }// beforeEach

    @Test
    @DisplayName("/api/logout : 사용자의 RefreshToken을 Redis에서 지우고 AccessToken을 블랙리스트에 추가하는데 성공한다.")
    void logout() throws Exception {
        // given
        User    user    = userRepository.findById(1L).orElse(null);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        String accessToken  = jwtProvider.generateToken(Duration.ofSeconds(10L), userDTO);
        String refreshToken = jwtProvider.generateToken(Duration.ofSeconds(60L), userDTO);

        RefreshToken redisRefreshToken = RefreshToken.builder().id(user.getId()).refreshToken(refreshToken).expiration(60L).build();

        refreshTokenRedisRepository.save(redisRefreshToken);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andDo(print());

        Optional<RefreshToken> byId1 = refreshTokenRedisRepository.findById(user.getId());
        Optional<LogoutToken > byId2 = logoutTokenRedisRepository.findById(accessToken);

        assertThat(byId1.isPresent()).isFalse();
        assertThat(byId2.isPresent()).isTrue();
        assertThat(byId2.get().getId()).isEqualTo(accessToken);

        Long restTime1 = (jwtProvider.getExpiration(accessToken).getTime() - (new Date().getTime())) / 1000;
        Long restTime2 = byId2.get().getExpiration();

        assertThat(restTime1).isEqualTo(restTime2);
    }// logout

}// LogoutProcessingFilterTest


















