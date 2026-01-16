package com.in.mzncvmc.common.system.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {


    /**
     * 사용자 토큰 정보를 Cookie 에 저장한다.
     *
     * @param tokenName, accessToken, response
     */
    public void insertAccessTokenCookie(String tokenName, String accessToken, HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenName, accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 적용 시 true
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1시간

        System.out.println("insertAccessTokenCookie.accessToken : " + accessToken );

        response.addCookie(cookie);
    }

    /**
     * 사용자 토큰 정보를 Cookie 에서 삭제한다.
     *
     * @param tokenName, response
     */
    public void deleteAccessTokenCookie(String tokenName, HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 즉시 만료

        System.out.println("deleteAccessTokenCookie.accessToken : null" );

        response.addCookie(cookie);
    }

    /**
     * 사용자 토큰 정보를 Cookie 에서 추출한다.
     *
     * @param request
     */
    public String extractAccessTokenCookie(String tokenName, HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (tokenName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
