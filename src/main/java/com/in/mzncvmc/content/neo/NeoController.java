package com.in.mzncvmc.content.neo;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/m/neo")
public class NeoController {
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
