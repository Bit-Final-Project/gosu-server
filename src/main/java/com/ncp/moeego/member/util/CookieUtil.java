package com.ncp.moeego.member.util;

import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void createCookie(HttpServletResponse response, String key, String value, Integer expiredS) {
        // SameSite=None을 사용하여 크로스 사이트 요청에서도 쿠키 전송
        String cookieString = String.format("%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=None", 
                                            key, value, expiredS);
        response.addHeader("Set-Cookie", cookieString);
    }
}
