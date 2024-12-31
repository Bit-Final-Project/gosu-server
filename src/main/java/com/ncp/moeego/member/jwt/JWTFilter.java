package com.ncp.moeego.member.jwt;

import com.ncp.moeego.member.bean.JwtDTO;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.repository.RefreshRepository;
import com.ncp.moeego.member.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 로그 확인
        System.out.println("요청 URI: " + request.getRequestURI());
        System.out.println("요청 메서드: " + request.getMethod());
        
        String authorizationHeader = request.getHeader("authorization");

        // Authorization 헤더가 없는 경우 필터 체인 통과
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String access = authorizationHeader.substring(7); // "Bearer " 제거
        String email;

        try {
            // Access 토큰 유효성 검증
            jwtUtil.isExpired(access);
            String category = jwtUtil.getCategory(access);
            if (!"access".equals(category)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Access 토큰에서 이메일 추출
            email = jwtUtil.getEmail(access);
        } catch (ExpiredJwtException e) {
            // Access 토큰 만료 시 Refresh 토큰 확인
            String refresh = Arrays.stream(request.getCookies())
                    .filter(cookie -> "refresh".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (refresh == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh token not found. Please login again.");
                return;
            }

            try {
                // Refresh 토큰 검증
                jwtUtil.isExpired(refresh);
                String category = jwtUtil.getCategory(refresh);
                if (!"refresh".equals(category) || !refreshRepository.existsByRefresh(refresh)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid or expired refresh token. Please login again.");
                    return;
                }

                // Refresh 토큰에서 이메일 추출
                email = jwtUtil.getEmail(refresh);

                // Access 토큰 재발급
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
                String newAccess = jwtUtil.createJwt("access", new JwtDTO(
                        member.getMemberNo(),
                        member.getEmail(),
                        member.getName(),
                        member.getAddress(),
                        member.getProfileImage(),
                        member.getPhone(),
                        member.getMemberStatus()
                ), member.getMemberStatus().name(), 60 * 10 * 1000L);

                response.setHeader("authorization", "Bearer " + newAccess);

            } catch (ExpiredJwtException ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh token expired. Please login again.");
                return;
            }
        }

        // SecurityContext에 인증 정보 설정
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        MemberDetails memberDetails = new MemberDetails(member);
        System.out.println("filter : " + memberDetails);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터 체인 진행
        filterChain.doFilter(request, response);
    }
}

