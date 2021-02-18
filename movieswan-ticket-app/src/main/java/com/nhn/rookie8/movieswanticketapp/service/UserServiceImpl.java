package com.nhn.rookie8.movieswanticketapp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserServiceImpl implements UserService{

    @Override
    public String getUidFromSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        if(session == null || session.getAttribute("uid") == null){
            return null;
        }

        return (String) session.getAttribute("uid");
    }
}
