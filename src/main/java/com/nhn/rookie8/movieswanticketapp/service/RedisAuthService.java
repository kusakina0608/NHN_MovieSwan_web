package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
@ConditionalOnProperty(name="auth", havingValue = "redis", matchIfMissing = true)
public class RedisAuthService implements AuthService {
    private final RedisTemplate<String, Object> redis;

    @Override
    public Cookie setSession(MemberIdNameDTO member) {
        Cookie cookie = this.createCookie();
        if (isSessionExist(cookie.getValue()))
            return null;

        redis.opsForValue().set(cookie.getValue(), member, Duration.ofMinutes(10));
        return cookie;
    }

    @Override
    public void updateSession(String authKey) {
        if (authKey == null) return;
        redis.expire(authKey, Duration.ofMinutes(10));
    }

    @Override
    public void expireSession(String authKey) {
        if (authKey == null) return;
        redis.delete(authKey);
    }

    @Override
    public boolean isSessionExist(String authKey) {
        return redis.opsForValue().size(authKey) != 0;
    }

    @Override
    public MemberIdNameDTO getMemberInfo(String authKey) {
        if (!isSessionExist(authKey))
            return MemberIdNameDTO.builder().build();

        return (MemberIdNameDTO) redis.opsForValue().get(authKey);
    }
}