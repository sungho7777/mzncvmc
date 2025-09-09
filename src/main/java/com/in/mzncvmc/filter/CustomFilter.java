package com.in.mzncvmc.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest request
            , ServletResponse response
            , FilterChain chain) throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String uri = httpRequest.getRequestURI();
        //logger.debug("Requested URI: " + uri);

        // 요청 전 처리
        //System.out.println("Before filter logic");

        // 다음 필터 또는 컨트롤러 호출
        chain.doFilter(request, response);

        // 요청 후 처리
        //System.out.println("After filter logic");
    }
}
