package com.in.mzncvmc.content.userHistory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_history",
        indexes = {
                @Index(name = "idx_user_time", columnList = "userId, createdAt"),
                @Index(name = "idx_action_type", columnList = "actionType")
        })
@Getter
@Setter
@NoArgsConstructor
public class UserHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 20)
    private String actionType; // LOGIN, LOGOUT, CREATE, READ, UPDATE, DELETE

    @Column(nullable = false, length = 500)
    private String uri;

    @Column(nullable = false, length = 10)
    private String httpMethod;

    @Column(length = 100)
    private String controllerMethod;

    @Column(columnDefinition = "TEXT")
    private String requestData; // 요청 파라미터나 중요 데이터

    @Column(length = 50)
    private String clientIp;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static UserHistory createLoginHistory(String username, String clientIp) {
        UserHistory history = new UserHistory();
        history.username = username;
        history.actionType = "LOGIN";
        history.uri = "/login";
        history.httpMethod = "POST";
        history.clientIp = clientIp;
        history.createdAt = LocalDateTime.now();
        return history;
    }

    public static UserHistory createLogoutHistory(String username, String clientIp) {
        UserHistory history = new UserHistory();
        history.username = username;
        history.actionType = "LOGOUT";
        history.uri = "/logout";
        history.httpMethod = "POST";
        history.clientIp = clientIp;
        history.createdAt = LocalDateTime.now();
        return history;
    }

    public static UserHistory createCrudHistory(String username, String actionType, String uri,
                                                String httpMethod, String controllerMethod,
                                                String requestData, String clientIp) {
        UserHistory history = new UserHistory();
        history.username = username;
        history.actionType = actionType;
        history.uri = uri;
        history.httpMethod = httpMethod;
        history.controllerMethod = controllerMethod;
        history.requestData = requestData;
        history.clientIp = clientIp;
        history.createdAt = LocalDateTime.now();
        return history;
    }
}
