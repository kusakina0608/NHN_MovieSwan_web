package com.nhn.rookie8.movieswanticketapp.configuration;

import com.nhn.rookie8.movieswanticketapp.interceptor.MemberAuthInterceptor;
import com.nhn.rookie8.movieswanticketapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@Order(value=5)
public class MvcConfiguration implements WebMvcConfigurer {

    // Pattern 추가로 검사할 영역을 지정할 수 있습니다.
    private final List<String> pattern = new ArrayList<>(Arrays.asList("/mypage/**", "/reserve/**", "/api/seat/**"));
    private final AuthService authService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberAuthInterceptor(authService))
                .addPathPatterns(pattern);
    }
}
