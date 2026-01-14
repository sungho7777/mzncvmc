package com.in.mzncvmc.common.auth.login;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.common.system.constants.CommonConstants.RECOVERY;
import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_RECOVERY;

@Log4j2
@Controller
@RequestMapping(SLASH_RECOVERY)
public class RecoveryController {

    @GetMapping()
    public String goPage() {

        return RECOVERY;
    }
}
