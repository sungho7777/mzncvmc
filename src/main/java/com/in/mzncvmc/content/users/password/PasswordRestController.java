package com.in.mzncvmc.content.users.password;

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
@RequestMapping(SLASH_API + "/password")
public class PasswordRestController {

    @Autowired
    private final PasswordService passwordService;


    // 사용자 비밀번호 변경
    @PostMapping("/change")
    public ApiResponse<?> change(@RequestBody PasswordChangeDto passwordChangeDto,
                                         Authentication authentication) {

        return passwordService.change(authentication.getName(), passwordChangeDto);
    }

    // 이메일을 통한 사용자 비밀번호 초기화
    @PostMapping("/recovery")
    public ApiResponse<?> recovery(@Valid @RequestBody PasswordRecoveryDto passwordRecoveryDto,
                                           HttpServletRequest request) {

        return passwordService.recovery(passwordRecoveryDto);
    }

    // 이메일 복구 코드 입력 후 비밀번호 초기화
    @PostMapping("/reset")
    public ApiResponse<?> reset(@Valid @RequestBody PasswordRecoveryDto passwordRecoveryDto,
                                        HttpServletRequest request) {
        // TODO. 복구 코드가 맞는지 확인 해야 한다.

        return passwordService.reset(passwordRecoveryDto);
    }
}
