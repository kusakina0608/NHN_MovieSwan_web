package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Auth;
import com.nhn.rookie8.movieswanticketapp.entity.QAuth;
import com.nhn.rookie8.movieswanticketapp.repository.AuthRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@ConditionalOnProperty(name="auth", havingValue = "db")
public class DBAuthService implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Override
    public Cookie setSession(MemberIdNameDTO member) {
        Cookie cookie = this.createCookie();
        if (isSessionExist(cookie.getValue()))
            return null;

        authRepository.save(toEntity(cookie.getValue(), member));
        return cookie;
    }

    @Override
    public void updateSession(String authKey) {
        authRepository.save(toEntity(authKey, getMemberInfo(authKey)));
    }

    @Override
    public void expireSession(String authKey) {
        if (authRepository.existsById(authKey))
            authRepository.deleteById(authKey);
    }

    @Override
    public boolean isSessionExist(String authKey) {
        return authRepository.existsById(authKey);
    }

    @Override
    public MemberIdNameDTO getMemberInfo(String authKey) {
        if (!isSessionExist(authKey))
            return MemberIdNameDTO.builder().build();

        return entityToMemberIdNameDTO(authRepository.findById(authKey).get());
    }

    @Scheduled(fixedDelay = 300000)
    public void deleteExpiredAuthKey(){
        List<Auth> list = authRepository.findAll(
                getExpiredAuthKey(), PageRequest.of(0, 1000)).toList();

        authRepository.deleteInBatch(list);
    }

    private BooleanBuilder getExpiredAuthKey() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QAuth qAuth = QAuth.auth;
        BooleanExpression expression = qAuth.regDate.lt(LocalDateTime.now().minusMinutes(30));
        booleanBuilder.and(expression);
        return booleanBuilder;
    }

    private Auth toEntity(String key, MemberIdNameDTO member) {
        return Auth.builder()
                .authKey(key)
                .memberId(member.getMemberId())
                .name(member.getName())
                .build();
    }

    private MemberIdNameDTO entityToMemberIdNameDTO(Auth entity) {
        return MemberIdNameDTO.builder()
                .memberId(entity.getMemberId())
                .name(entity.getName())
                .build();
    }
}