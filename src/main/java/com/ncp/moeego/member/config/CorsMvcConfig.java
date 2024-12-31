package com.ncp.moeego.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // API 요청용 CORS 설정
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "https://www.moeego.site",
                    "https://server.moeego.site",
                    "https://kauth.kakao.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

        // OAuth2 콜백용 CORS 설정
        registry.addMapping("/login/oauth2/code/**")
                .allowedOrigins(
                    "https://www.moeego.site",
                    "https://server.moeego.site"
                )
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
