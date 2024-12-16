package com.ncp.moeego.member.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.moeego.member.bean.JwtDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.MemberInfoProvider;
import com.ncp.moeego.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.jwt.JWTUtil;
import com.ncp.moeego.member.service.RefreshTokenService;
import com.ncp.moeego.member.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomFormSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final MemberInfoProvider memberInfoProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MemberDetails member = (MemberDetails) authentication.getPrincipal();
        String detailsEmail = member.getUsername();

        JwtDTO jwtDTO = memberInfoProvider.getJwtDtoByEmail(detailsEmail);

        String memberStatus = authentication.getAuthorities().iterator().next().getAuthority();

        // access
        String access = jwtUtil.createJwt("access", jwtDTO, memberStatus, 10 * 1000L);
        response.setHeader("access", access);

        // refresh
        Integer expireS = 24 * 60 * 60;
        String refresh = jwtUtil.createJwt("refresh", jwtDTO, memberStatus, expireS * 1000L);
        response.addCookie(CookieUtil.createCookie("refresh", refresh, expireS));

        refreshTokenService.saveRefresh(jwtDTO.getEmail(), jwtDTO.getName(), expireS, refresh);

        // json 을 ObjectMapper 로 직렬화하여 전달
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("name", jwtDTO.getName());
        responseData.put("email", jwtDTO.getEmail());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), responseData);
    }
}