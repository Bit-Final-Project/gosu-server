package com.ncp.moeego.member.config;

import com.ncp.moeego.member.jwt.JWTFilter;
import com.ncp.moeego.member.jwt.JWTUtil;
import com.ncp.moeego.member.oauth2.CustomFormSuccessHandler;
import com.ncp.moeego.member.oauth2.CustomLogoutFilter;
import com.ncp.moeego.member.oauth2.CustomOAuth2SuccessHandler;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.repository.RefreshRepository;
import com.ncp.moeego.member.service.MemberInfoProvider;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.member.service.RefreshTokenService;
import com.ncp.moeego.member.service.oauth2.OAuth2MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final OAuth2MemberService oAuth2MemberService;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                System.out.println("exception = " + exception);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // disable
        http
                .httpBasic((basic) -> basic.disable())
                .csrf((csrf) -> csrf.disable());

        // form
        http
                .formLogin((form) -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email") // `email` 필드를 `username`으로 매핑
                        .passwordParameter("pwd")
                        .successHandler(new CustomFormSuccessHandler(jwtUtil, refreshTokenService, memberInfoProvider))
                        .failureHandler(authenticationFailureHandler())
                        .permitAll());

        // oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint((userinfo) -> userinfo
                                .userService(oAuth2MemberService))
                        .successHandler(new CustomOAuth2SuccessHandler(jwtUtil, refreshTokenService, memberInfoProvider))
                        .failureHandler(authenticationFailureHandler())
                        .permitAll());

        // logout
        http
                .logout((auth) -> auth
                        .logoutSuccessUrl("/")
                        .permitAll());

        // cors
        http
                .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Arrays.asList(
                                "http://211.188.56.236:80",
                                "http://211.188.48.106:8080"
                        ));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("access"));

                        return configuration;
                    }
                }));

        // authorization
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/join", "/logout", "/oauth2-jwt-header", "/**").permitAll()    //개발 진행을 위한 전체 권한 허용/추후 나누기("/**")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        // 인가되지 않은 사용자에 대한 exception -> 프론트엔드로 코드 응답
        http.exceptionHandling((exception) ->
                exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }));

        // jwt filter
        http
                .addFilterAfter(new JWTFilter(jwtUtil, memberRepository, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        // custom logout filter 등록
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        // session stateless
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
