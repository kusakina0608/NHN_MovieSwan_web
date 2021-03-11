package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.*;

public interface MemberService {

    MemberDTO getMemberInfoById(String memberId);

    boolean checkResponse(MemberResponseDTO memberResponseDTO);

    MemberResponseDTO register(MemberRegisterDTO memberRegisterDTO);

    MemberResponseDTO auth(MemberAuthDTO memberAuthDTO);

    MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO);
}
