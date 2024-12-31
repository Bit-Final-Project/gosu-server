package com.ncp.moeego.member.util;

import jakarta.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CookieUtil {

    public static Cookie createCookie(String key, String value, Integer expiredS) {
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(key, encodedValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(expiredS);
        cookie.setSecure(true); // HTTPS에서만 전송
        cookie.setDomain(".moeego.site");
        return cookie;
    }
}
