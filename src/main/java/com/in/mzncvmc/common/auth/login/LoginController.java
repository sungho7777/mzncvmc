package com.in.mzncvmc.common.auth.login;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.common.system.constants.CommonConstants.LOGIN;
import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_LOGIN;

@Log4j2
@Controller
@RequestMapping(SLASH_LOGIN)
public class LoginController {

    @GetMapping()
    public String goPage() {
        // TODO. jwt 토큰 인증 후 바로 login 페이지로 이동하겠금 수정 할 것.

        return LOGIN;
    }
}
