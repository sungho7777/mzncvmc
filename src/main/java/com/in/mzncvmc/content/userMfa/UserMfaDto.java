package com.in.mzncvmc.content.userMfa;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMfaDto {
    private Long id;
    private Long userId;
    private boolean mfaEnabled;
    private String mfaSecret;
    private boolean mfaVerified;
    private int failedAttempts;
    private String backupCodes;
}
