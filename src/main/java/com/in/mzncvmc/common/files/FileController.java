package com.in.mzncvmc.common.files;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.content.common.CommonConstants.*;
import static com.in.mzncvmc.content.common.CommonConstants.CONTENT_PAGE;

@Log4j2
@Controller
@RequestMapping("/files")
public class FileController {
    private final String FILES = "files";
    private final String MENU_LIST_JSP = "files/list.jsp";


    @GetMapping(SLASH_LIST)
    public String listPage(Model model) {

        model.addAttribute(SIDEBAR, FILES);
        model.addAttribute(CONTENT_PAGE, MENU_LIST_JSP);
        return MAIN;
    }
}
