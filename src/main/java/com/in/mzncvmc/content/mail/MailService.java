package com.in.mzncvmc.content.mail;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MailService {

    public void sendUserOtpMail(String email, String otp) {

        log.info("MailService.sendOtpMail.otp : " + otp);
        log.info("MailService.sendOtpMail.email : " + email);
    }
}
