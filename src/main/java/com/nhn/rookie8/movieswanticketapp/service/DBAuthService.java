package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Auth;
import com.nhn.rookie8.movieswanticketapp.entity.QAuth;
import com.nhn.rookie8.movieswanticketapp.repository.AuthRepository;
import com.nhn.rookie8.movieswanticketapp.ticketexception.AlreadyExpiredOrNotExistKeyErrorException;
import com.nhn.rookie8.movieswanticketapp.ticketexception.AlreadyKeyExistException;
import com.nhn.rookie8.movieswanticketapp.ticketexception.InvalidAuthKeyErrorException;
import com.nhn.rookie8.movieswanticketapp.ticketexception.SessionNotExistErrorException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@ConditionalOnProperty(name="auth", havingValue = "db")
public class DBAuthService implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Override
    public boolean saveMemberInfo(String authKey, MemberIdNameDTO member) {
        if (isKeyExist(authKey))
            throw new AlreadyKeyExistException();

        Auth auth = toEntity(authKey, member);
        authRepository.save(auth);

        log.info("Registered AuthKey : {}", authKey);

        return true;
    }

    @Override
    public boolean expireAuth(String authKey) {
        if(!isKeyExist(authKey))
            throw new AlreadyExpiredOrNotExistKeyErrorException();

        authRepository.deleteById(authKey);

        log.info("Expired AuthKey : {}", authKey);

        return true;
    }

    @Override
    public MemberIdNameDTO readMemberInfo(String authKey) {
        if (!isKeyExist(authKey))
            throw new SessionNotExistErrorException();

        Optional<Auth> result = authRepository.findById(authKey);

        log.info("Member Info : {}", result.get());

        return MemberIdNameDTO.builder()
                .memberId(result.get().getMemberId())
                .name(result.get().getName())
                .build();
    }

    @Override
    public boolean existSession(String authKey) {
        return authKey != null && authKey.length() == 32
                && isKeyExist(authKey);
    }

    @Override
    public boolean isKeyExist(String authKey) {
        return authRepository.findById(authKey).isPresent();
    }

    @Scheduled(fixedDelay = 300000)
    public void deleteExpiredAuthKey(){
        BooleanBuilder booleanBuilder = getExpiredAuthKey();
        Pageable pageable = PageRequest.of(0, 1000);

        List<Auth> list = authRepository.findAll(booleanBuilder, pageable).toList();
        list.forEach(e -> log.info("Expired AuthKey : {}", e));

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
}
