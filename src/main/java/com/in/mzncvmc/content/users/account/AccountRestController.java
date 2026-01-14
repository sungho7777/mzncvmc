package com.in.mzncvmc.content.users.account;

import com.in.mzncvmc.common.auth.login.LoginRequest;
import com.in.mzncvmc.common.system.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_API;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(SLASH_API + "/account")
public class AccountRestController {

    @Autowired
    private final AccountService accountService;
/*
    @Autowired
    public AccountRestController(AccountService accountService){
        this.accountService = accountService;
    }
*/

    @PostMapping("/changePassword")
    public ApiResponse<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
                                         Authentication authentication) {

        return accountService.changePassword(authentication.getName(), changePasswordDto);
    }

    // 비밀번호 초기화 인증 없이 허용
    /*
    @PostMapping("/resetPassword_bak")
    public ApiResponse<?> resetPassword_bak(@Valid @RequestBody LoginRequest loginRequest,
                                        HttpServletRequest request) {

        return accountService.resetPassword(loginRequest);
    }
*/
    // 이메일을 통한 사용자 비밀번호 초기화
    @PostMapping("/recoveryPassword")
    public ApiResponse<?> recoveryPassword(@Valid @RequestBody RecoveryPasswordDto recoveryPasswordDto,
                                           HttpServletRequest request) {

        return accountService.recoveryPassword(recoveryPasswordDto);

    }

    // 이메일 복구 코드 입력 후 비밀번호 초기화
    @PostMapping("/resetPassword")
    public ApiResponse<?> resetPassword(@Valid @RequestBody RecoveryPasswordDto recoveryPasswordDto,
                                            HttpServletRequest request) {
        // TODO. 복구 코드가 맞는지 확인 해야 한다.

        return accountService.resetPassword(recoveryPasswordDto);
    }
}
