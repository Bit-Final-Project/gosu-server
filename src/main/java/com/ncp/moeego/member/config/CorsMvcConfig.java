package com.ncp.moeego.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                    .allowedOriginPatterns("*") // 모든 출처 허용 (패턴 기반)
                    .allowedMethods("*") // 모든 HTTP 메서드 허용
                    .allowedHeaders("*") // 모든 헤더 허용
                    .exposedHeaders("Set-Cookie") // 노출할 헤더
                    .allowCredentials(true); // 쿠키 허용
    }
}
