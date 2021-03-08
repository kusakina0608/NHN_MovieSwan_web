package com.nhn.rookie8.movieswanticketapp.interceptor;

import com.nhn.rookie8.movieswanticketapp.service.RedisAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Log4j2
public class MemberAuthInterceptor extends HandlerInterceptorAdapter {
    private final RedisAuthService redisAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie[] cookies = request.getCookies();
        String authKey = new String();

        if (cookies != null)
            for (int i = 0; i < cookies.length; i++)
                if (cookies[i].getName().equals("SWANAUTH")) {
                    authKey = cookies[i].getValue();
                    break;
                }

        if (!redisAuthService.validMemberInfo(authKey)){
            response.sendRedirect("/member/login");
            return false;
        }

        request.setAttribute("memberId", redisAuthService.readMemberInfo(authKey).getMemberId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
            throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
