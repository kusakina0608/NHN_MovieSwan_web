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
    private AuthRepository repository;

    @Override
    public void saveMemberInfo(String authKey, MemberIdNameDTO member) {
        try {
            Auth auth = toEntity(authKey, member);
            repository.save(auth);

            log.info("Registered AuthKey : {}", auth);
        } catch(Exception e) {
            log.error(e);
        }
    }

    @Override
    public void expireAuth(String authKey) {
        try {
            repository.deleteById(authKey);

            log.info("Expired AuthKey : {}", authKey);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public MemberIdNameDTO readMemberInfo(String authKey) {
        try {
            Optional<Auth> result = repository.findById(authKey);

            log.info("Member Info : {}", result.isPresent() ? result.get() : "No Result");

            return result.isPresent() ?
                    MemberIdNameDTO.builder()
                            .memberId(result.get().getMemberId())
                            .name(result.get().getName())
                            .build()
                    : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public boolean validMemberInfo(String authKey) {
        try {
            return authKey != null && repository.findById(authKey).isPresent();
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @Scheduled(fixedDelay = 300000)
    public void deleteExpiredAuthKey(){
        try {
            BooleanBuilder booleanBuilder = getExpiredAuthKey();
            Pageable pageable = PageRequest.of(0, 1000);

            List<Auth> list = repository.findAll(booleanBuilder, pageable).toList();
            list.forEach(e -> log.info("Expired AuthKey : {}", e));

            repository.deleteInBatch(list);
        } catch (Exception e) {
            log.error(e);
        }
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
