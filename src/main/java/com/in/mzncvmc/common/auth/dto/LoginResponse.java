package com.in.mzncvmc.common.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private String pwNotifyDuration;
    private String tokenType;

    // 생성자
    public LoginResponse() {}

    public LoginResponse(String accessToken, String refreshToken, Long userId, String username, String pwNotifyDuration,String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.pwNotifyDuration = pwNotifyDuration;
        this.tokenType = tokenType;
    }
}
