package com.in.mzncvmc.common.auth.oAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthUserInfo {
    private String providerId;
    private String email;
    private String name;
    private String profileImage;
    private String provider;
}
