package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberResponseDTO;

import java.util.Map;

public interface MemberService {

    MemberDTO getMemberInfoById(String memberId);

    boolean checkResponse(MemberResponseDTO response);

    MemberResponseDTO register(Map<String, String[]> requestMap);

    MemberResponseDTO auth(Map<String, String[]> requestMap);

    MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO);
}
