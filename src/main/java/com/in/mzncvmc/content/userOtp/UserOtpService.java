package com.in.mzncvmc.content.userOtp;

import com.in.mzncvmc.content.users.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Log4j2
@Service
public class UserOtpService {

    @Value("${user.otp.fail.count}")
    private Integer userOtpFailCount;

    private UserOtpRepository userOtpRepository;
    private UsersRepository usersRepository;

    @Autowired
    public UserOtpService(UserOtpRepository userOtpRepository, UsersRepository usersRepository){
        this.userOtpRepository = userOtpRepository;
        this.usersRepository = usersRepository;
    }

    public String generateUserOtp() {
        return String.format("%06d",
                new SecureRandom().nextInt(1_000_000));
    }
    /**
     * Otp 사용자 대상
     *
     * @param userId 사용자 아이디
     * @return Optional<Vo> 데이터 엔티티
     */
    @Transactional
    public void createUserOtp(Long userId, String otpCode) {

        UserOtp entity = new UserOtp();
        entity.setUserId(userId);
        entity.setOtpCode(otpCode);
        entity.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        entity.setUsedYn("N");
        entity.setFailCount(0);
        entity.setCreatedAt(LocalDateTime.now());

        userOtpRepository.save(entity);
    }

    @Transactional
    public void verifyUserOtp(Long userId, String otpCode) {

        UserOtp entity = userOtpRepository
                .findTopByUserIdOrderByCreatedAtDescLimit1(userId)
                .orElseThrow(() -> new RuntimeException("OTP 없음"));

        // 이미 사용됨
        if ("Y".equals(entity.getUsedYn())) {
            throw new RuntimeException("이미 사용된 OTP");
        }

        // 만료
        if (entity.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP 만료");
        }

        // 실패 횟수 제한
        if (entity.getFailCount() >= userOtpFailCount) {
            throw new RuntimeException("OTP 실패 횟수 초과");
        }

        // OTP 불일치
        if (!entity.getOtpCode().equals(otpCode)) {
            entity.setFailCount(entity.getFailCount() + 1);
            throw new RuntimeException("OTP 불일치");
        }

        // 성공 처리
        entity.setUsedYn("Y");
    }
}
