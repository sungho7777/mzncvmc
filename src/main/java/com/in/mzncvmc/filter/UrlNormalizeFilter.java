package com.in.mzncvmc.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class UrlNormalizeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        // // 중복 슬래시 제거
        String normalizedPath = path.replaceAll("/{2,}", "/");

        if (!normalizedPath.equals(path)) {
            // 리다이렉트로 정규화 URL 사용
            ((HttpServletResponse) response).sendRedirect(normalizedPath);
            return;
        }

        chain.doFilter(request, response);
    }
}
