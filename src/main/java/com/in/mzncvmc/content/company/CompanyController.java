package com.in.mzncvmc.content.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.in.mzncvmc.content.common.constants.CommonConstants.*;
import static com.in.mzncvmc.content.common.constants.StringConstants.*;

@Controller
@RequestMapping("/m/company")
public class CompanyController {
    private final String COMPANY = "company";
    private final String COMPANYS = "companys";
    private final String MENU_LIST_JSP = "company/list.jsp";
    private final String MENU_VIEW_JSP = "company/view.jsp";
    private final String MENU_AMEND_JSP = "company/amend.jsp";

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping(SLASH_LIST)
    public String listPage(Model model) {

        setCommonAttributes(model, MENU_LIST_JSP);
        return MAIN;
    }

    @GetMapping(SLASH_VIEW_ID)
    public String viewPage(@PathVariable Long id, Model model) {

        model.addAttribute(ID, id);
        setCommonAttributes(model, MENU_VIEW_JSP);
        return MAIN;
    }

    @GetMapping(SLASH_AMEND_ID)
    public String amendPage(@PathVariable Long id, @RequestParam String mapping, Model model) {

        model.addAttribute(ID, id);
        model.addAttribute(MAPPING, mapping);
        setCommonAttributes(model, MENU_AMEND_JSP);
        return MAIN;
    }

    private void setCommonAttributes(Model model, String contentPage) {
        model.addAttribute(SIDEBAR, COMPANYS);
        model.addAttribute(SUB_SIDEBAR, EMPTY);
        model.addAttribute(CONTENT_PAGE, contentPage);
    }
}
