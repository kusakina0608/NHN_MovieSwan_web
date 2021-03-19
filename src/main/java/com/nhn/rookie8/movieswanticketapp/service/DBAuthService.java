package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Auth;
import com.nhn.rookie8.movieswanticketapp.entity.AuthCheck;
import com.nhn.rookie8.movieswanticketapp.entity.QAuth;
import com.nhn.rookie8.movieswanticketapp.entity.QAuthCheck;
import com.nhn.rookie8.movieswanticketapp.repository.AuthCheckRepository;
import com.nhn.rookie8.movieswanticketapp.repository.AuthRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@ConditionalOnProperty(name="auth", havingValue = "db")
public class DBAuthService implements AuthService {

    private final AuthRepository authRepository;

    private final AuthCheckRepository authCheckRepository;

    @Override
    @Transactional
    public Cookie setSession(MemberIdNameDTO member) {

        if(isAuthCheckSessionExist(member.getMemberId())){
            expireSessionByMemberId(member.getMemberId());
        }

        Cookie cookie = this.createCookie();
        while(isAuthSessionExist(cookie.getValue())) cookie = createCookie();

        authRepository.save(toAuthEntity(cookie.getValue(), member));
        authCheckRepository.save(toAuthCheckEntity(cookie.getValue(),member.getMemberId()));
        return cookie;
    }

    @Override
    @Transactional
    public void updateSessionByAuthKey(String authKey) {
        MemberIdNameDTO memberIdNameDTO = getMemberInfoByAuthKey(authKey);
        authRepository.save(toAuthEntity(authKey, memberIdNameDTO));
        authCheckRepository.save(toAuthCheckEntity(authKey, memberIdNameDTO.getMemberId()));
    }

    @Override
    @Transactional
    public void expireSessionByAuthKey(String authKey) {
        authCheckRepository.deleteById(getMemberInfoByAuthKey(authKey).getMemberId());
        authRepository.deleteById(authKey);
    }

    @Override
    @Transactional
    public void expireSessionByMemberId(String memberId) {
        authRepository.deleteById(getAuthKeyByMemberId(memberId));
        authCheckRepository.deleteById(memberId);
    }

    @Override
    public boolean isAuthSessionExist(String authKey) {
        return authRepository.existsById(authKey);
    }

    @Override
    public boolean isAuthCheckSessionExist(String memberId) {
        return authCheckRepository.existsById(memberId);
    }

    @Override
    public MemberIdNameDTO getMemberInfoByAuthKey(String authKey) {
        if (!isAuthSessionExist(authKey))
            return MemberIdNameDTO.builder().build();

        return entityToMemberIdNameDTO(authRepository.findById(authKey).get());
    }

    @Override
    public String getAuthKeyByMemberId(String memberId){
        if (!isAuthCheckSessionExist(memberId))
            return null;
        return authCheckRepository.findById(memberId).get().getAuthKey();
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void deleteExpiredAuthKey(){
        List<Auth> authList = authRepository.findAll(
                getExpiredAuthKey(), PageRequest.of(0, 1000)).toList();

        List<AuthCheck> authCheckList = authCheckRepository.findAll(
                getExpiredAuthCheckKey(), PageRequest.of(0, 1000)).toList();

        authRepository.deleteInBatch(authList);
        authCheckRepository.deleteInBatch(authCheckList);
    }

    private BooleanBuilder getExpiredAuthKey() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QAuth qAuth = QAuth.auth;
        BooleanExpression expression = qAuth.regDate.lt(LocalDateTime.now().minusMinutes(30));
        booleanBuilder.and(expression);
        return booleanBuilder;
    }

    private BooleanBuilder getExpiredAuthCheckKey() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QAuthCheck qAuthCheck = QAuthCheck.authCheck;
        BooleanExpression expression = qAuthCheck.regDate.lt(LocalDateTime.now().minusMinutes(30));
        booleanBuilder.and(expression);
        return booleanBuilder;
    }

    private Auth toAuthEntity(String key, MemberIdNameDTO member) {
        return Auth.builder()
                .authKey(key)
                .memberId(member.getMemberId())
                .name(member.getName())
                .build();
    }

    private AuthCheck toAuthCheckEntity(String key, String memberId) {
        return AuthCheck.builder()
                .authKey(key)
                .memberId(memberId)
                .build();
    }

    private MemberIdNameDTO entityToMemberIdNameDTO(Auth entity) {
        return MemberIdNameDTO.builder()
                .memberId(entity.getMemberId())
                .name(entity.getName())
                .build();
    }
}