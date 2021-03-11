package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.Cookie;

public interface AuthService {

    public void saveMemberInfo(String authKey, MemberIdNameDTO member);

    public void expireAuth(String authKey);

    public MemberIdNameDTO readMemberInfo(String authKey);

    public boolean validMemberInfo(String authKey);

    default Cookie createCookie() {
        String cookieValue = RandomStringUtils.randomAlphanumeric(32);
        Cookie cookie = new Cookie("SWANAUTH", cookieValue);
        cookie.setMaxAge(-1);
        cookie.setPath("/");

        return cookie;
    }

    default String getAuthKey(Cookie[] cookies) {
        for (int i = 0; i < cookies.length; i++)
            if (cookies[i].getName().equals("SWANAUTH"))
                return cookies[i].getValue();
        return null;
    }
}
