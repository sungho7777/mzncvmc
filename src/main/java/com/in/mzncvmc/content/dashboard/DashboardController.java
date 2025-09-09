package com.in.mzncvmc.content.dashboard;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping("/dashboardSystem")
    public String dashboardSystemPage(Model model) {

        model.addAttribute("sidebar", "dashboard");
        model.addAttribute("contentPage", "dashboard/dashboardSystem.jsp");
        return "main";
    }
}
