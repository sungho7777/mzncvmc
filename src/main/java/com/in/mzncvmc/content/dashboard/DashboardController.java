package com.in.mzncvmc.content.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.content.common.CommonConstants.*;

@Controller
@RequestMapping("/m/dashboard")
public class DashboardController {
    private final String DASHBOARD = "dashboard";
    private final String MENU_DASHBOARD_JSP = "dashboard/dashboard.jsp";


    @GetMapping(SLASH_DASHBOARD)
    public String listPage(Model model) {

        model.addAttribute(SIDEBAR, DASHBOARD);
        model.addAttribute(CONTENT_PAGE, MENU_DASHBOARD_JSP);
        return MAIN;
    }
}
