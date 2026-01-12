package com.in.mzncvmc.content.userMfa;

import lombok.Getter;

@Getter
public class UserMfaVerifyDto {
    private String username;
    private String mfaCode;
    private String mfaType; // "mfaCodeRadio", "backupCodeRadio"

}
