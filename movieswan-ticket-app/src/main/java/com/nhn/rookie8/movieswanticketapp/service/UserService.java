package com.nhn.rookie8.movieswanticketapp.service;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    String getUidFromSession(HttpServletRequest request);
}
