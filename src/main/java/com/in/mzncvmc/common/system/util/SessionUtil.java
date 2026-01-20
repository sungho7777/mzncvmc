package com.in.mzncvmc.common.system.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

    /**
     * 사용자 토큰 정보를 Session 에 저장한다.
     *
     * @param tokenName, accessToken, response
     */
    public void insertAccessTokenSession(String tokenName, String accessToken, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute(tokenName, accessToken);

    }

    public void insertUserDetailsSession(String loginUser, UserDetails userDetails, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(loginUser, userDetails);
    }
}
