package com.in.mzncvmc.content.dcs.dcsLog;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.common.system.constants.CommonConstants.*;
import static com.in.mzncvmc.common.system.constants.CommonConstants.CONTENT_PAGE;
import static com.in.mzncvmc.common.system.constants.CommonConstants.SUB_SIDEBAR;
import static com.in.mzncvmc.common.system.constants.StringConstants.EMPTY;

@Log4j2
@Controller
@RequestMapping("/m/dcs/dcsLog")
public class DcsLogController {
    private final String DCS_LOG = "dcsLog";
    private final String MENU_LIST_JSP = "dcs/dcsLog/list.jsp";
    private final String MENU_VIEW_JSP = "dcs/dcsLog/view.jsp";

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

    /**
     * V.상세화면 화면이동 (view)
     *
     * @param !model
     * @return MAIN
     */
    @GetMapping(SLASH_VIEW_ID)
    public String viewPage(@PathVariable Long id, Model model) {

        model.addAttribute(ID, id);
        setCommonAttributes(model, MENU_VIEW_JSP);
        return MAIN;
    }

    private void setCommonAttributes(Model model, String contentPage) {
        model.addAttribute(SIDEBAR, DCS_LOG);
        model.addAttribute(SUB_SIDEBAR, EMPTY);
        model.addAttribute(CONTENT_PAGE, contentPage);
    }
}
