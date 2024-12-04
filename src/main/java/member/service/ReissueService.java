package member.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import member.jwt.JWTUtil;
import member.repository.RefreshRepository;
import member.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ReissueService {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        refresh = Arrays.stream(cookies).filter((cookie) -> cookie.getName().equals("refresh"))
                .findFirst().get().getValue();

        // 쿠키에 refresh 토큰 x
        if (refresh == null) {
            return new ResponseEntity<>("refresh token is null", HttpStatus.BAD_REQUEST);
        }

        // 만료된 토큰은 payload 읽을 수 없음 -> ExpiredJwtException 발생
        try {
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e){
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // refresh 토큰이 아님
        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getEmail(refresh);
        String name = jwtUtil.getName(refresh);
        String memberStatus = jwtUtil.getMemberStatus(refresh);

        // refresh DB 조회
        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        // DB 에 없는 리프레시 토큰 (혹은 블랙리스트 처리된 리프레시 토큰)
        if(!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Integer expiredS = 60 * 60 * 24;
        // new tokens
        String newAccess = jwtUtil.createJwt("access", email, name, memberStatus, 60 * 10 * 1000L);
        String newRefresh = jwtUtil.createJwt("refresh", email, name, memberStatus, expiredS * 1000L);

        // 기존 refresh DB 삭제, 새로운 refresh 저장
        refreshRepository.deleteByRefresh(refresh);
        refreshTokenService.saveRefresh(email, name, expiredS, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(CookieUtil.createCookie("refresh", newRefresh, expiredS));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
