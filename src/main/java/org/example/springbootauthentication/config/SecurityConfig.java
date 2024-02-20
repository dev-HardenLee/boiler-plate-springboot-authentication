package org.example.springbootauthentication.config;

import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.filter.security.JwtAuthenticationFilter;
import org.example.springbootauthentication.filter.security.JwtRefreshTokenFilter;
import org.example.springbootauthentication.filter.security.LoginProcessingFilter;
import org.example.springbootauthentication.filter.security.LogoutProcessingFilter;
import org.example.springbootauthentication.handler.CustomAuthenticationFailureHandler;
import org.example.springbootauthentication.handler.CustomAuthenticationSuccessHandler;
import org.example.springbootauthentication.handler.JwtAuthenticationFailureHandler;
import org.example.springbootauthentication.provider.JwtProvider;
import org.example.springbootauthentication.provider.CustomAuthenticationProvider;
import org.example.springbootauthentication.repository.LogoutTokenRedisRepository;
import org.example.springbootauthentication.repository.RefreshTokenRedisRepository;
import org.example.springbootauthentication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String loginUrl = "/api/login";

    private static final String logoutUrl = "/api/logout";

    private static final String refreshTokenUrl = "/api/refresh-token";

    private final AuthenticationConfiguration authenticationConfiguration;

    private final UserDetailsService customUserDetailsService;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final LogoutTokenRedisRepository logoutTokenRedisRepository;

    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    private final ModelMapper modelMapper;

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web
                .ignoring()
                .requestMatchers(toH2Console());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);
        http
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        http.userDetailsService(customUserDetailsService);

        http.addFilterBefore(loginProcessingFilter()  , UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(logoutProcessingFilter() , UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtRefreshTokenFilter()  , UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.GET , "/api/home" ).authenticated()
                        .requestMatchers(HttpMethod.GET , "/api/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET , "/api/user" ).hasRole("USER")
                        .anyRequest().permitAll()
                );


        return http.build();
    }// securityFilterChain

    @Bean
    public LoginProcessingFilter loginProcessingFilter() throws Exception {
        LoginProcessingFilter loginProcessingFilter = new LoginProcessingFilter(loginUrl, HttpMethod.POST);

        loginProcessingFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        loginProcessingFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler());
        loginProcessingFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());
//        loginProcessingFilter.setAuthenticationDetailsSource(); // 인증시에 요청자의 추가 정보를 담을 때 사용함.

        return loginProcessingFilter;
    }// loginProcessingFilter

    @Bean
    public LogoutProcessingFilter logoutProcessingFilter() {
        return new LogoutProcessingFilter(new AntPathRequestMatcher(logoutUrl, HttpMethod.GET.name()), jwtProvider, refreshTokenRedisRepository, logoutTokenRedisRepository, jwtAuthenticationFailureHandler);
    }// logoutProcessingFilter

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userRepository, logoutTokenRedisRepository, jwtProvider, jwtAuthenticationFailureHandler, modelMapper);
    }

    @Bean
    public JwtRefreshTokenFilter jwtRefreshTokenFilter() {
        return new JwtRefreshTokenFilter(new AntPathRequestMatcher(refreshTokenUrl, HttpMethod.POST.name()), jwtProvider, refreshTokenRedisRepository, jwtAuthenticationFailureHandler, userRepository, modelMapper);
    }


    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, bCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(jwtProvider, refreshTokenRedisRepository);
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}// SecurityConfig
