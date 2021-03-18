package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.Cookie;

public interface AuthService {

    String cookieName = "SWANAUTH";

    Integer cookieAge = 10 * 60;

    Cookie setSession(MemberIdNameDTO member);

    void updateSessionByAuthKey(String authKey);


    void expireSessionByAuthKey(String authKey);

    void expireSessionByMemberId(String memberId);

    boolean isAuthSessionExist(String authKey);

    boolean isAuthCheckSessionExist(String memberId);

    MemberIdNameDTO getMemberInfoByAuthKey(String authKey);

    String getAuthKeyByMemberId(String memberId);

    default Cookie createCookie() {
        Cookie cookie = new Cookie(cookieName,
                RandomStringUtils.randomAlphanumeric(32));

        cookie.setMaxAge(cookieAge);
        cookie.setPath("/");

        return cookie;
    }

    default Cookie getCookie(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies)
            if (cookie.getName().equals(cookieName))
                return cookie;
        return null;
    }

    default Cookie updateCookie(Cookie cookie) {
        cookie.setMaxAge(cookieAge);
        cookie.setPath("/");

        return cookie;
    }

    default Cookie expireCookie() {
        Cookie cookie = new Cookie(cookieName, null);

        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }

    default String getAuthKey(Cookie[] cookies) {
        if(cookies == null) return null;
        for (Cookie cookie : cookies)
            if (cookie.getName().equals(cookieName))
                return cookie.getValue();
        return null;
    }
}
