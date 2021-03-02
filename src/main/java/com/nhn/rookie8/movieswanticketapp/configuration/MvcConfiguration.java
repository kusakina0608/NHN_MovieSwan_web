package com.nhn.rookie8.movieswanticketapp.configuration;

import com.nhn.rookie8.movieswanticketapp.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    // Pattern 추가로 검사할 영역을 지정할 수 있습니다.
    private final List<String> pattern = new ArrayList<>(Arrays.asList("/mypage/**", "/reserve/**", "/api/seat/**"));

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserAuthInterceptor())
                .addPathPatterns(pattern);
    }
}
