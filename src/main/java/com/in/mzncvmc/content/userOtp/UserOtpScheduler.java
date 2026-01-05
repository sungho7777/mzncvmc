package com.in.mzncvmc.content.userOtp;

import com.in.mzncvmc.content.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
//@RequiredArgsConstructor
public class UserOtpScheduler {
    private UserOtpRepository userOtpRepository;

    @Autowired
    public UserOtpScheduler(UserOtpRepository userOtpRepository){
        this.userOtpRepository = userOtpRepository;
    }


    /**
     * 1분마다 실행
     * 만료된 OTP + 5분 지난 데이터 삭제
     */
    @Transactional
    @Scheduled(cron = "0 */1 * * * *")
    public void cleanupExpiredOtp() {

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);

        int deletedCount = userOtpRepository.deleteExpiredOtp(threshold);

        if (deletedCount > 0) {
            log.info("Expired OTP cleanup completed. deletedCount={}", deletedCount);
        }
    }

}
