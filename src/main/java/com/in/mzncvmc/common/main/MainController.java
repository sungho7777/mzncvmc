package com.in.mzncvmc.common.main;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.common.system.constants.CommonConstants.MAIN;
import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_MAIN;

@Log4j2
@Controller
@RequestMapping(SLASH_MAIN)
public class MainController {

    @GetMapping()
    public String goPage() {

        return MAIN;
    }
}
