package com.in.mzncvmc.content.neo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/neo")
public class NeoController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final NeoService neoService;

    public NeoController(NeoService neoService) {
        this.neoService = neoService;
    }

    @GetMapping("/list")
    public String listPage(Model model) {

        model.addAttribute("sidebar", "neo");
        model.addAttribute("contentPage", "neo/list.jsp");
        return "main";
    }






    @GetMapping("/view/{id}")
    public String viewPage(@PathVariable Long id, Model model) {
        Neo neo = neoService.findById(id);
        model.addAttribute("neo", neo);

        return "neo/view";
    }
    @GetMapping("/amend/{id}")
    public String amendPage(@PathVariable Long id, Model model) {
        Neo neo = neoService.findById(id);
        model.addAttribute("neo", neo);

        return "neo/amend";
    }

}
