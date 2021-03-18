package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.time.Duration;

@Service
@Log4j2
@ConditionalOnProperty(name="auth", havingValue = "redis", matchIfMissing = true)
public class RedisAuthService implements AuthService {

    @Autowired
    private RedisTemplate<String, MemberIdNameDTO> redisTemplateAuth;

    @Autowired
    @Qualifier("redisTemplateAuthCheck")
    private RedisTemplate<String, String> redisTemplateAuthCheck;


    @Override
    @Transactional
    public Cookie setSession(MemberIdNameDTO member) {

        //중복로그인 방지
        if(isAuthCheckSessionExist(member.getMemberId())){
            expireSessionByMemberId(member.getMemberId());
        }

        //세션 생성
        Cookie cookie = createCookie();
        while(isAuthSessionExist(cookie.getValue())) cookie = createCookie();

        redisTemplateAuth.opsForValue().set(cookie.getValue(), member, Duration.ofMinutes(10));
        redisTemplateAuthCheck.opsForValue().set(member.getMemberId(), cookie.getValue(), Duration.ofMinutes(10));

        return cookie;
    }

    @Override
    @Transactional
    public void updateSessionByAuthKey(String authKey) {
		if (authKey == null) return;
        redisTemplateAuth.expire(authKey, Duration.ofMinutes(10));
        redisTemplateAuthCheck.expire(getMemberInfoByAuthKey(authKey).getMemberId(), Duration.ofMinutes(10));

    }


    @Override
    @Transactional
    public void expireSessionByAuthKey(String authKey) {
		if (authKey == null) return;
        redisTemplateAuth.delete(authKey);
        redisTemplateAuthCheck.delete(getMemberInfoByAuthKey(authKey).getMemberId());
    }

    @Override
    @Transactional
    public void expireSessionByMemberId(String memberId) {
		if (memberId == null) return;
        redisTemplateAuth.delete(getAuthKeyByMemberId(memberId));
        redisTemplateAuthCheck.delete(memberId);
    }

    @Override
    public boolean isAuthSessionExist(String authKey) {
        return redisTemplateAuth.opsForValue().size(authKey) != 0;
    }

    @Override
    public boolean isAuthCheckSessionExist(String memberId) {
        return redisTemplateAuthCheck.opsForValue().size(memberId) != 0;
    }

    @Override
    public MemberIdNameDTO getMemberInfoByAuthKey(String authKey) {
        if (!isAuthSessionExist(authKey))
            return MemberIdNameDTO.builder().build();

        return redisTemplateAuth.opsForValue().get(authKey);
    }

    @Override
    public String getAuthKeyByMemberId(String memberId){
        if(!isAuthCheckSessionExist(memberId))
            return null;

        return redisTemplateAuthCheck.opsForValue().get(memberId);
    }
}
