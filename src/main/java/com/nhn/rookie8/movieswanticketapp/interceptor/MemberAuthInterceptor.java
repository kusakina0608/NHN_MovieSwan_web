package com.nhn.rookie8.movieswanticketapp.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Log4j2
public class MemberAuthInterceptor extends HandlerInterceptorAdapter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        HttpSession session = request.getSession();

        if (session != null && session.getAttribute("member") != null) {
            String memberId = objectMapper.convertValue
                    (session.getAttribute("member"), MemberIdNameDTO.class).getMemberId();

            request.setAttribute("memberId", memberId);
            return true;
        }

        response.sendRedirect("/member/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
        throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
