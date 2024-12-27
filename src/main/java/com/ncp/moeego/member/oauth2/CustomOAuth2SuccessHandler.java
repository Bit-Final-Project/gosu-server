package com.ncp.moeego.member.oauth2;

import com.ncp.moeego.member.bean.JwtDTO;
import com.ncp.moeego.member.bean.oauth2.OAuth2Member;
import com.ncp.moeego.member.service.MemberInfoProvider;
import com.ncp.moeego.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.jwt.JWTUtil;
import com.ncp.moeego.member.service.RefreshTokenService;
import com.ncp.moeego.member.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final MemberInfoProvider memberInfoProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        OAuth2Member customOAuth2User = (OAuth2Member) authentication.getPrincipal();

        String detailsEmail = customOAuth2User.getEmail(); // DB 저장용 식별자
        String memberStatus = authentication.getAuthorities().iterator().next().getAuthority();

        JwtDTO jwtDTO = memberInfoProvider.getJwtDtoByEmail(detailsEmail);
        
        System.out.println(jwtDTO.getMemberStatus().name() + jwtDTO.getName() + jwtDTO.getEmail());

        Integer expireS = 24 * 60 * 60;
        String access = jwtUtil.createJwt("access", jwtDTO, jwtDTO.getMemberStatus().name(), 60 * 10 * 1000L);
        String refresh = jwtUtil.createJwt("refresh", jwtDTO, jwtDTO.getMemberStatus().name(), expireS * 1000L);

        // refresh 토큰 DB 저장
        refreshTokenService.saveRefresh(jwtDTO.getEmail(), jwtDTO.getName(), expireS, refresh);

        response.addCookie(CookieUtil.createCookie("access", access, 60 * 10));
        response.addCookie(CookieUtil.createCookie("refresh", refresh, expireS));

        // redirect query param 인코딩 후 전달
        // 이후에 JWT 를 읽어서 데이터를 가져올 수도 있지만, JWT 파싱 비용이 많이 들기 때문에
        // 처음 JWT 발급할 때 이름을 함께 넘긴 후, 로컬 스토리지에 저장한다.
        // 요청의 Origin 헤더 확인
        String origin = request.getHeader("Origin");
        if (origin == null) {
            // Origin 헤더가 없으면 기본값으로 localhost 사용
            origin = "http://211.188.48.106";
        }

        // redirect query param 인코딩 후 전달
        String encodedName = URLEncoder.encode(jwtDTO.getName(), "UTF-8");
        response.sendRedirect(origin + "/oauth2-jwt-header?name=" + encodedName);
    }

}
