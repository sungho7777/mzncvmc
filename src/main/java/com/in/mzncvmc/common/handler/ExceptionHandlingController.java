package com.in.mzncvmc.common.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Log4j2
@Controller
public class ExceptionHandlingController implements ErrorController {
    private final String ERROR_ETC_PAGE_PATH = "/error/error";

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String handleError(HttpServletRequest request, Model model) {

        // 에러 코드를 획득한다.
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // 에러 코드에 대한 상태 정보
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());

            log.info("httpStatus : " + statusCode);

            // 404 error
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // 에러 페이지에 표시할 정보
                model.addAttribute("code", status.toString());
                model.addAttribute("msg", httpStatus.getReasonPhrase());
                model.addAttribute("timestamp", new Date());
                //return ERROR_ETC_PAGE_PATH;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // 500 error
                // 서버에 대한 에러이기 때문에 사용자에게 정보를 제공하지 않는다.
                //return ERROR_ETC_PAGE_PATH;
            } else {
                // error
            }
        }

        return ERROR_ETC_PAGE_PATH;
    }
}
