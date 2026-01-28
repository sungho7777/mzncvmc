package com.in.mzncvmc.content.users.password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRecoveryDto {
    private String username;
    private String email;
    private String recoveryCode;
}
