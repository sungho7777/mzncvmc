package com.in.mzncvmc.content.users.account;

import com.in.mzncvmc.common.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.in.mzncvmc.content.common.constants.CommonConstants.SLASH_API;

@Log4j2
@RestController
@RequestMapping(SLASH_API + "/account")
public class AccountRestController {

    private final AccountService accountService;

    @Autowired
    public AccountRestController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody PasswordChangeDto request,
            Authentication authentication
    ) {
        log.debug("changePassword. : " + authentication.getName());


        accountService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    // 비밀번호 초기화 인증 없이 허용
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.debug("resetPassword. : " + loginRequest.getUsername());

        accountService.resetPassword(loginRequest.getUsername());

        return ResponseEntity.ok().build();
    }
}
