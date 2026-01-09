package com.in.mzncvmc.common.system.mail;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MailService {

    public void sendUserOtpMail(String email, String otp) {

        log.info("MailService.sendOtpMail.otp : " + otp);
        log.info("MailService.sendOtpMail.email : " + email);
    }

    // MFA 확인 이메일 발송
    public void sendMFAConfirmationEmail(String email) {


        log.info("MailService.sendMFAConfirmationEmail.email : " + email);
    }
}
