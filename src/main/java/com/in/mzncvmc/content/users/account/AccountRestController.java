package com.in.mzncvmc.content.users.account;

import com.in.mzncvmc.common.login.LoginRequest;
import com.in.mzncvmc.common.system.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ApiResponse<?> changePassword(@RequestBody PasswordChangeDto request,Authentication authentication) {

        return accountService.changePassword(authentication.getName(), request);
    }

    // 비밀번호 초기화 인증 없이 허용
    @PostMapping("/resetPassword")
    public ApiResponse<?> resetPassword(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        return accountService.resetPassword(loginRequest.getUsername());
    }
}
