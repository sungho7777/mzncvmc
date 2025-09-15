package com.in.mzncvmc.common.config;

import com.in.mzncvmc.common.interceptor.CrudTrackingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final CrudTrackingInterceptor crudTrackingInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/main");
        registry.addRedirectViewController("", "/main");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crudTrackingInterceptor)
                .addPathPatterns("/api/**") // API 경로만 추적
                .excludePathPatterns("/api/login", "/api/logout", "/api/user/authorities"); // 로그인/로그아웃은 별도 처리
    }
}
