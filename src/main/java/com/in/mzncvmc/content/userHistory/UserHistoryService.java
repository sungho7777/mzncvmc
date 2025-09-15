package com.in.mzncvmc.content.userHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;

    @Transactional
    public void saveHistory(UserHistory history) {
        try {
            userHistoryRepository.save(history);
        } catch (Exception e) {
            // 히스토리 저장 실패가 메인 기능에 영향주면 안됨
            log.error("Failed to save user history: {}", e.getMessage());
        }
    }

    public void saveLogin(String userId, String clientIp) {
        saveHistory(UserHistory.createLoginHistory(userId, clientIp));
    }

    public void saveLogout(String userId, String clientIp) {
        saveHistory(UserHistory.createLogoutHistory(userId, clientIp));
    }

    public void saveCrudAction(String userId,
                               String actionType,
                               String uri,
                               String httpMethod,
                               String controllerMethod,
                               String requestData,
                               String clientIp) {
        saveHistory(UserHistory.createCrudHistory(
                userId,
                actionType,
                uri,
                httpMethod,
                controllerMethod,
                requestData,
                clientIp));
    }
}
