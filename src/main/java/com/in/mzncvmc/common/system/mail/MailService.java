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

    public void sendChangePassword(String email){

        log.info("MailService.sendChangePassword.text : 비밀번호 변경 성공");
        log.info("MailService.sendChangePassword.email : " + email);
    }
    public void sendResetPassword(String email){

        log.info("MailService.sendResetPassword.text : 비밀번호 초기화 성공");
        log.info("MailService.sendResetPassword.email : " + email);
    }

    public  void sendResetUserMfa(String email){

        log.info("MailService.sendResetUserMfa.text : 사용자 Mfa 초기화 성공");
        log.info("MailService.sendResetUserMfa.email : " + email);
    }

}
