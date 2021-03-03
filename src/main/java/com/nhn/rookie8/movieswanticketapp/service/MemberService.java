package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberResponseDTO;

public interface MemberService {

    MemberDTO getUserInfoById(String memberId);

    boolean checkResponse(MemberResponseDTO response);
}
