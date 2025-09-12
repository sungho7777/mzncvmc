package com.in.mzncvmc.content.users;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.in.mzncvmc.content.common.CommonConstants.*;

@Log4j2
@Controller
@RequestMapping("/m/users")
public class UsersController {
    private final String USER = "user";
    private final String USERS = "users";
    private final String MENU_LIST_JSP = "users/list.jsp";
    private final String MENU_VIEW_JSP = "users/view.jsp";
    private final String MENU_AMEND_JSP = "users/amend.jsp";

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping(SLASH_LIST)
    public String listPage(Model model) {

        setCommonAttributes(model, MENU_LIST_JSP);
        return MAIN;
    }

    @GetMapping(SLASH_VIEW_ID)
    public String viewPage(@PathVariable Long id, Model model) {
        log.debug("UsersController.viewPage : " + id);

        UsersDto dto = usersService.findById(id);

        model.addAttribute(USER, dto);
        setCommonAttributes(model, MENU_VIEW_JSP);
        return MAIN;
    }

    @GetMapping(SLASH_AMEND_ID)
    public String amendPage(@PathVariable Long id, @RequestParam String mapping, Model model) {
        UsersDto dto = (id != null && id > 0) ? usersService.findById(id) : new UsersDto();
        model.addAttribute(USER, dto);

        model.addAttribute(MAPPING, mapping);
        setCommonAttributes(model, MENU_AMEND_JSP);
        return MAIN;
    }

    private void setCommonAttributes(Model model, String contentPage) {
        model.addAttribute(SIDEBAR, USERS);
        model.addAttribute(CONTENT_PAGE, contentPage);
    }
}
