package com.in.mzncvmc.common.auth.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String statusLogin;
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private String pwNotifyDuration;
    private String tokenType;
    private String message;

    // 생성자
    public LoginResponse() {}

    public LoginResponse(String statusLogin, String accessToken, String refreshToken, Long userId, String username, String pwNotifyDuration,String tokenType, String message) {
        this.statusLogin = statusLogin;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.pwNotifyDuration = pwNotifyDuration;
        this.tokenType = tokenType;
        this.message = message;
    }
}
