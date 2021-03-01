package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;

public interface UserService {

    MemberDTO getUserInfoById(String memberId);
}
