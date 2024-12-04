package com.ncp.moeego.member.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String access = null;
        access = request.getHeader("access");

        // access token null
        if (access == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // access token expired
        try{
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(access);

        // not access token
        if(!category.equals("access")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtUtil.getEmail(access);
        String memberStatus = jwtUtil.getMemberStatus(access);

        Member member = Member.builder()
                .email(email)
                .pwd("temp_pw")
                .memberStatus(MemberStatus.valueOf(memberStatus))
                .build();

        MemberDetails memberDetails = new MemberDetails(member);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}