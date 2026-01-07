package com.in.mzncvmc.common.system.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request
            , ServletResponse response
            , FilterChain chain) throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String uri = httpRequest.getRequestURI();
        log.debug("Requested URI: " + uri);

        // 요청 전 처리
        //System.out.println("Before filter logic");

        // 다음 필터 또는 컨트롤러 호출
        chain.doFilter(request, response);

        // 요청 후 처리
        //System.out.println("After filter logic");
    }
}
