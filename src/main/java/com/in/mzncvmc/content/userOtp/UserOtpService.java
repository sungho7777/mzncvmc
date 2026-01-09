package com.in.mzncvmc.content.userOtp;

import com.in.mzncvmc.common.system.response.VerificationResponse;
import com.in.mzncvmc.content.userMfa.UserMfa;
import com.in.mzncvmc.content.users.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

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

    /**
     * Mail OTP 코드 검증
     *
     * @param userId, mfaCode, otpCode
     * @return VerificationResponse
     */
    @Transactional
    public VerificationResponse verifyUserOtp(Long userId, String otpCode) {
        Optional<UserOtp> optional = userOtpRepository.findTopByUserIdOrderByCreatedAtDescLimit1(userId);
        if (optional.isEmpty()) {
            return new VerificationResponse(false,
                    // "OTP가 설정되지 않았습니다. 먼저 OTP 코드를 설정해 주세요."
                    "OTP has not been set up. Please set up an OTP code first.",
                    true);
        }
        UserOtp userOtp = optional.get();

        // 이미 사용됨
        if ("Y".equals(userOtp.getUsedYn())) {
            return new VerificationResponse(false,
                    // "이 OTP 코드는 이미 사용되었습니다."
                    "This OTP code has already been used.",
                    false);
        }

        // 만료
        if (userOtp.getExpiredAt().isBefore(LocalDateTime.now())) {
            return new VerificationResponse(false,
                    // "OTP 코드가 이미 만료되었습니다."
                    "The OTP code has already expired.",
                    false);
        }

        // 실패 횟수 제한
        if (userOtp.getFailCount() >= userOtpFailCount) {
            return new VerificationResponse(false,
                    // "OTP 실패 횟수가 초과되었습니다."
                    "OTP failure count exceeded.",
                    false);
        }

        // OTP 불일치
        if (!userOtp.getOtpCode().equals(otpCode)) {
            userOtp.setFailCount(userOtp.getFailCount() + 1);
            return new VerificationResponse(false,
                    // "입력하신 OTP 코드가 일치하지 않습니다."
                    "The entered OTP code does not match.",
                    false);
        }

        // 성공 처리
        return new VerificationResponse(true,
                // "OTP 코드가 성공적으로 활성화되었습니다."
                "OTP code has been successfully activated.",
                false);
    }
}