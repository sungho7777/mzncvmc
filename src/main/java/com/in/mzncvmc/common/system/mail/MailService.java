package com.in.mzncvmc.common.system.mail;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class MailService {

    public void sendGenerateUserOtp(String email, String otp) {

        log.info("MailService.sendGenerateUserOtp.text : 설정을 위한 인증 코드가 이메일로 전송되었습니다.");
        log.info("MailService.sendGenerateUserOtp.email : " + email);
    }

    public void sendUserOtpMail(String email, String otp) {
        log.info("MailService.sendOtpMail.text : 인증 코드가 이메일로 전송되었습니다.");

        System.out.println(otp);
        log.info("MailService.sendOtpMail.email : " + email);
    }

    // MFA 확인 이메일 발송
    public void sendMFAConfirmationEmail(String email, List<String> backupCodes) {

        log.info("MailService.sendMFAConfirmationEmail.text : MFA가 성공적으로 활성화되었습니다. 다시 로그인 시도하세요.");
        List<Map<String, Object>> backupCodeList = new ArrayList<>();
        for (String code : backupCodes) {

            System.out.println(code);
        }
        log.info("MailService.sendMFAConfirmationEmail.email : " + email);
    }

    public void sendUserMfaFailCountLock(String email){

        log.info("MailService.sendUserMfaFailCountLock.text : 로그인 시도 횟수가 너무 많습니다. 계정이 30분간 잠금 처리됩니다.");
        log.info("MailService.sendUserMfaFailCountLock.email : " + email);
    }
    public void sendUserMfaFailCountInterruption(String email){

        log.info("MailService.sendUserMfaFailCountInterruption.text : 인증 시도 횟수가 너무 많아 실패했습니다. MFA 설정이 일시 중단되었습니다. 관리자에게 문의하십시오.");
        log.info("MailService.sendUserMfaFailCountInterruption.email : " + email);
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
