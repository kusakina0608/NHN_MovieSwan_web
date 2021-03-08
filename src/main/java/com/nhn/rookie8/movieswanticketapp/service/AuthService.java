package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Auth;

public interface GiveMeNameService {

    public void saveMemberInfo(String authKey, MemberIdNameDTO member);

    public void expireAuth(String authKey);

    public MemberIdNameDTO readMemberInfo(String authKey);

    public boolean validMemberInfo(String authKey);

    default Auth toEntity(String key, MemberIdNameDTO member) {
        return Auth.builder()
                .authKey(key)
                .memberId(member.getMemberId())
                .name(member.getName())
                .build();
    }
}
