package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.Cookie;

public interface AuthService {

    public static final String cookieName = "SWANAUTH";

    public static final Integer cookieAge = 10 * 60;

    public Cookie setSession(MemberIdNameDTO member);

    public void updateSession(String authKey);

    public void expireSession(String authKey);

    public boolean isSessionExist(String authKey);

    public MemberIdNameDTO getMemberInfo(String authKey);

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
