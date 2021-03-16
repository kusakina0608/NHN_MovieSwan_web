package com.nhn.rookie8.movieswanticketapp.interceptor;

import com.nhn.rookie8.movieswanticketapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Log4j2
public class MemberAuthInterceptor extends HandlerInterceptorAdapter {
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authKey = authService.getAuthKey(request.getCookies());

        if (authKey == null || !authService.isSessionExist(authKey)) {
            response.sendRedirect("/member/login");
            return false;
        }

        authService.updateSession(authKey);
        response.addCookie(authService.updateCookie(authService.getCookie(request.getCookies())));

        request.setAttribute("memberId", authService.getMemberInfo(authKey).getMemberId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
            throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
