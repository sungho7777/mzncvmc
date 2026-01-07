package com.in.mzncvmc.content.ai.ollama;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.in.mzncvmc.common.system.constants.CommonConstants.*;

@Log4j2
@Controller
@RequestMapping("/m/ai/ollama")
public class OllamaController {
    private final String AI = "ai";
    private final String OLLAMA = "ollama";
    private final String MENU_LIST_JSP = "ai/ollama/list.jsp";

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
        model.addAttribute(SIDEBAR, AI);
        model.addAttribute(SUB_SIDEBAR, OLLAMA);
        model.addAttribute(CONTENT_PAGE, contentPage);
    }
}
