package com.in.mzncvmc.common.otp;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class OtpCleanupScheduler {

    @Autowired
    private OtpVerificationRepository otpVerificationRepository;

    /*
        매일 새벽 3시에 7일 이상 지난 OTP 삭제
    */
    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void cleanupExpiredOtps() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        otpVerificationRepository.deleteExpiredOtps(threshold);
    }
}
