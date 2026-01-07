package com.in.mzncvmc.content.bbs.posts;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.in.mzncvmc.common.system.constants.CommonConstants.*;

@Log4j2
@Controller
@RequestMapping("/m/bbs/bbsPosts")
public class BbsPostsController {
    private final String BBS = "bbs";
    private final String POSTS = "bbsPosts";
    private final String MENU_LIST_JSP = "bbs/bbsPosts/list.jsp";
    private final String MENU_VIEW_JSP = "bbs/bbsPosts/view.jsp";
    private final String MENU_AMEND_JSP = "bbs/bbsPosts/amend.jsp";

    /**
     * L.리스트 화면이동 (list)
     *
     * @param !model
     * @return MAIN
     */
    @GetMapping(SLASH_LIST)
    public String listPage(@RequestParam(value = "categoryId", required = false) String categoryId, Model model) {

        model.addAttribute("categoryId", categoryId);

        setCommonAttributes(model, categoryId, MENU_LIST_JSP);
        return MAIN;
    }

    /**
     * V.상세화면 화면이동 (view)
     *
     * @param !model
     * @return MAIN
     */
    @GetMapping(SLASH_VIEW_ID)
    public String viewPage(@PathVariable Long id, @RequestParam(value = "categoryId", required = false) String categoryId, Model model) {

        model.addAttribute(ID, id);
        model.addAttribute("categoryId", categoryId);

        setCommonAttributes(model, categoryId, MENU_VIEW_JSP);
        return MAIN;
    }

    /**
     * A.수정화면 화면이동 (amend)
     *
     * @param !model
     * @return MAIN
     */
    @GetMapping(SLASH_AMEND_ID)
    public String amendPage(@PathVariable Long id, @RequestParam String mapping, @RequestParam(value = "categoryId", required = false) String categoryId, Model model) {

        model.addAttribute(ID, id);
        model.addAttribute(MAPPING, mapping);
        model.addAttribute("categoryId", categoryId);

        setCommonAttributes(model, categoryId, MENU_AMEND_JSP);
        return MAIN;
    }


    private void setCommonAttributes(Model model, String categoryId, String contentPage) {
        model.addAttribute(SIDEBAR, BBS);
        model.addAttribute(SUB_SIDEBAR, POSTS + (categoryId == null || categoryId.isBlank() ? "" : categoryId));
        model.addAttribute(CONTENT_PAGE, contentPage);
    }
}
