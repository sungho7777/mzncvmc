package com.in.mzncvmc.content.sbadmin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sbadmin")
public class SbadminController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/index")
    public String indexPage(Model model) {
        model.addAttribute("sidebar", "index");

        return "sbadmin/index";
    }
    @GetMapping("/blank")
    public String blankPage(Model model) {
        model.addAttribute("sidebar", "blank");

        return "sbadmin/blank";
    }
    @GetMapping("/tables")
    public String tablesPage(Model model) {
        model.addAttribute("sidebar", "tables");

        return "sbadmin/tables";
    }
}
