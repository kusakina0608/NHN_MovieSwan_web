package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.*;

public interface MemberService {

    MemberDTO getMemberInfoById(String memberId);

    boolean checkResponse(MemberResponseDTO response);

    MemberResponseDTO register(MemberRegisterDTO request);

    MemberResponseDTO auth(MemberAuthDTO request);

    MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO);
}
