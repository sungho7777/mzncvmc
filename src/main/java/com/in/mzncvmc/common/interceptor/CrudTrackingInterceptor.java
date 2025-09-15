package com.in.mzncvmc.common.interceptor;

import com.in.mzncvmc.common.auth.util.JwtUtil;
import com.in.mzncvmc.content.userHistory.UserHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrudTrackingInterceptor implements HandlerInterceptor {
    private final UserHistoryService userHistoryService;
    private final JwtUtil jwtUtil;

    // CRUD 관련 경로만 추적
    private final Set<String> trackingPaths = Set.of("/api/");

    // HTTP 메소드별 액션 타입 매핑
    private String getActionType(String httpMethod) {
        switch (httpMethod.toUpperCase()) {
            case "POST": return "CREATE";
            case "GET": return "READ";
            case "PUT":
            case "PATCH": return "UPDATE";
            case "DELETE": return "DELETE";
            default: return "OTHER";
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        // 추적 대상 경로인지 확인
        if (!shouldTrack(request)) {
            return;
        }

        // 성공한 요청만 기록 (에러는 별도 로그로)
        if (response.getStatus() >= 400) {
            return;
        }

        String userId = extractUserId(request);
        if ("anonymous".equals(userId)) {
            return; // 익명 사용자는 기록하지 않음
        }

        String actionType = getActionType(request.getMethod());
        String controllerMethod = getControllerMethod(handler);
        String requestData = getRequestData(request);

        userHistoryService.saveCrudAction(
                userId,
                actionType,
                request.getRequestURI(),
                request.getMethod(),
                controllerMethod,
                requestData,
                getClientIp(request)
        );
    }

    private boolean shouldTrack(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return trackingPaths.stream().anyMatch(uri::startsWith);
    }

    private String extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                return jwtUtil.extractUsername(token.substring(7));
            } catch (Exception e) {
                log.debug("Failed to extract user from token", e);
            }
        }
        return "anonymous";
    }

    private String getControllerMethod(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            return hm.getBeanType().getSimpleName() + "." + hm.getMethod().getName();
        }
        return null;
    }

    private String getRequestData(HttpServletRequest request) {
        // 간단한 파라미터만 기록 (POST body는 제외 - 민감정보 때문에)
        return request.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .reduce((a, b) -> a + "&" + b)
                .orElse("");
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
