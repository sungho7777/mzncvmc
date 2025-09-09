package com.in.mzncvmc.content.login;

import com.in.mzncvmc.content.users.Users;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LoginService loginService;
    private final MessageSource messageSource;

    public LoginController(LoginService loginService, MessageSource messageSource) {
        this.loginService = loginService;
        this.messageSource = messageSource;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/main";
        return "login"; // /WEB-INF/views/login.jsp
    }


    @PostMapping("/login")
    public String login(@RequestParam String userid,
                        @RequestParam String password,
                        HttpSession session,
                        Model model,
                        Locale locale) {

        Users users = loginService.login(userid, password);

        if (users != null) {
            session.setAttribute("users", users);
            //model.addAttribute("user", user);
            return "redirect:/main"; // -> main.jsp
        }

        String errorMsg = messageSource.getMessage("error.login", null, locale);
        model.addAttribute("errorMsg", errorMsg);

        logger.debug("errorMsg : " + errorMsg);
        logger.debug("locale : " + locale);

        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/login";
    }
}
