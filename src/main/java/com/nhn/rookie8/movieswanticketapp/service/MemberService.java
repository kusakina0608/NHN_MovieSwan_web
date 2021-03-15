package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.*;

public interface MemberService {

    MemberDTO getMemberInfoById(String memberId);

    boolean checkResponse(MemberResponseDTO memberResponseDTO);

    MemberResponseDTO register(MemberRegisterDTO memberRegisterDTO);

    TokenResponseDTO loginService(MemberAuthDomainDTO memberAuthDomainDTO);

    MemberResponseDTO verifyToken(String token);

    MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO);

    MemberAuthDTO domainToAuthDTO(MemberAuthDomainDTO memberAuthDomainDTO);
}
