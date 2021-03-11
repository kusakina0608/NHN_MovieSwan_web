package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
@ConditionalOnProperty(name="auth", havingValue = "redis", matchIfMissing = true)
public class RedisAuthService implements AuthService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOps;

    @PostConstruct
    public void initValueOps() {
        valueOps = redisTemplate.opsForValue();
    }

    @Override
    public void saveMemberInfo(String authKey, MemberIdNameDTO member) {
        try {
            valueOps.setIfAbsent(authKey, member, Duration.ofMinutes(30));

            log.info("Registered AuthKey : {}", authKey);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public void expireAuth(String authKey) {
        try {
            valueOps.setIfPresent(authKey, null, Duration.ofMillis(1));

            log.info("Expired AuthKey : {}", authKey);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public MemberIdNameDTO readMemberInfo(String authKey) {
        try {
            MemberIdNameDTO member = (MemberIdNameDTO) valueOps.get(authKey);

            log.info("Member Info : {}", member != null ? member : "No Result");

            return member;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public boolean validMemberInfo(String authKey) {
        try {
            return authKey != null && valueOps.get(authKey) != null;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
