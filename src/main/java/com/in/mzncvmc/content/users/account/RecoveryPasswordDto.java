package com.in.mzncvmc.content.users.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryPasswordDto {
    private String username;
    private String email;
    private String recoveryCode;
}
