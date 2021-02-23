package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.UserDTO;
import org.springframework.stereotype.Service;

public interface UserService {

    UserDTO getUserInfoById(String uid);
}
