package com.in.mzncvmc.content.main;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        Object users = session.getAttribute("users");
        if (users == null) {
            return "redirect:/login"; // 세션 없으면 다시 로그인
        }
        model.addAttribute("users", users);

        return "main"; // -> main.jsp
    }
}
