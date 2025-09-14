package com.in.mzncvmc.content.bbs.posts;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.content.common.CommonConstants.*;
import static com.in.mzncvmc.content.common.CommonConstants.CONTENT_PAGE;
import static com.in.mzncvmc.content.common.CommonConstants.SUB_SIDEBAR;

@Log4j2
@Controller
@RequestMapping("/m/bbs/posts")
public class BbsPostsController {
    private final String BBS = "bbs";
    private final String POSTS = "posts";
    private final String MENU_LIST_JSP = "bbs/posts/list.jsp";
    private final String MENU_VIEW_JSP = "bbs/posts/view.jsp";
    private final String MENU_AMEND_JSP = "bbs/posts/amend.jsp";

    @GetMapping(SLASH_LIST)
    public String listPage(Model model) {

        setCommonAttributes(model, MENU_LIST_JSP);
        return MAIN;
    }



    private void setCommonAttributes(Model model, String contentPage) {
        model.addAttribute(SIDEBAR, BBS);
        model.addAttribute(SUB_SIDEBAR, POSTS);
        model.addAttribute(CONTENT_PAGE, contentPage);
    }
}
