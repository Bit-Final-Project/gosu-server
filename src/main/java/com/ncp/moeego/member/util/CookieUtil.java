package com.ncp.moeego.member.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    public static Cookie createCookie(String key, String value, Integer expiredS) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS 연결에서만
        cookie.setMaxAge(expiredS);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}
