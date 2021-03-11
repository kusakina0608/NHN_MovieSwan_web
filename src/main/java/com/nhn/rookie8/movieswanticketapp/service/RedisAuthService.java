package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.ticketexception.AlreadyExpiredOrNotExistKeyErrorException;
import com.nhn.rookie8.movieswanticketapp.ticketexception.AlreadyKeyExistException;
import com.nhn.rookie8.movieswanticketapp.ticketexception.InvalidAuthKeyErrorException;
import com.nhn.rookie8.movieswanticketapp.ticketexception.SessionNotExistErrorException;
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
    public boolean saveMemberInfo(String authKey, MemberIdNameDTO member) {
        if (isKeyExist(authKey))
            throw new AlreadyKeyExistException();

        valueOps.setIfAbsent(authKey, member, Duration.ofMinutes(30));

        log.info("Registered AuthKey : {}", authKey);

        return true;
    }

    @Override
    public boolean expireAuth(String authKey) {
        if (!isKeyExist(authKey))
            throw new AlreadyExpiredOrNotExistKeyErrorException();

        valueOps.setIfPresent(authKey, null, Duration.ofMillis(1));

        log.info("Expired AuthKey : {}", authKey);

        return true;
    }

    @Override
    public MemberIdNameDTO readMemberInfo(String authKey) {
        if (!isKeyExist(authKey))
            throw new SessionNotExistErrorException();

        MemberIdNameDTO member = (MemberIdNameDTO) valueOps.get(authKey);

        log.info("Member Info : {}", member);

        return member;
    }

    @Override
    public boolean existSession(String authKey) {
        return authKey != null && authKey.length() == 32
                && isKeyExist(authKey);
    }

    @Override
    public boolean isKeyExist(String authKey) {
        return valueOps.get(authKey) != null;
    }
}