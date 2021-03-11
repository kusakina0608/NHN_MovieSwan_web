package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.ticketexception.InvalidAuthKeyErrorException;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.Cookie;

public interface AuthService {

    public boolean saveMemberInfo(String authKey, MemberIdNameDTO member);

    public boolean expireAuth(String authKey);

    public MemberIdNameDTO readMemberInfo(String authKey);

    public boolean isKeyExist(String authKey);

    public boolean existSession(String authKey);

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

        throw new InvalidAuthKeyErrorException();
    }
}
