package com.in.mzncvmc.common.system.menu;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import static com.in.mzncvmc.common.system.constants.CommonConstants.*;
import static com.in.mzncvmc.common.system.constants.CommonConstants.CONTENT_PAGE;
import static com.in.mzncvmc.common.system.constants.CommonConstants.SUB_SIDEBAR;
import static com.in.mzncvmc.common.system.constants.StringConstants.EMPTY;

@Log4j2
@Controller
@RequestMapping("/m/menu")
public class AdminMenuController {
    private final String MENU = "menu";
    private final String MENU_LIST_JSP = "menu/list.jsp";

    /**
     * L.리스트 화면이동 (list)
     *
     * @param !model
     * @return MAIN
     */
    @GetMapping(SLASH_LIST)
    public String listPage(Model model) {

        setCommonAttributes(model, MENU_LIST_JSP);
        return MAIN;
    }




    private void setCommonAttributes(Model model, String contentPage) {
        model.addAttribute(SIDEBAR, MENU);
        model.addAttribute(SUB_SIDEBAR, EMPTY);
        model.addAttribute(CONTENT_PAGE, contentPage);
    }
}
