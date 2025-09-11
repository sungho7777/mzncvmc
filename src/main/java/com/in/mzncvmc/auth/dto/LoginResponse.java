package com.in.mzncvmc.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String tokenType;

    // 생성자
    public LoginResponse() {}

    public LoginResponse(String accessToken, String refreshToken, String username, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.tokenType = tokenType;
    }
}
