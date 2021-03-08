package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Log4j2
@ConditionalOnProperty(name="auth", havingValue = "redis", matchIfMissing = true)
public class RedisAuthService implements AuthService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveMemberInfo(String key, MemberIdNameDTO member) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, member, Duration.ofMinutes(30));
    }

    @Override
    public void expireAuth(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, null, Duration.ofMillis(1));
    }

    @Override
    public MemberIdNameDTO readMemberInfo(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        MemberIdNameDTO member = (MemberIdNameDTO) valueOperations.get(key);
        return member;
    }

    @Override
    public boolean validMemberInfo(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return key != null && valueOperations.get(key) != null;
    }
}
