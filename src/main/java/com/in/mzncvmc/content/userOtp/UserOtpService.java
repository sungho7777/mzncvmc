package com.in.mzncvmc.content.userOtp;

import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserOtpService {
    @Value("${user.otp.fail.count}")
    private Integer userOtpFailCount;

    @Autowired
    private final UserOtpRepository userOtpRepository;
    @Autowired
    private UsersService usersService;

    /**
     * Otp 번호를 생성한다.
     *
     * @param
     * @return String otp
     */
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
     * @param userOtpVerifyDto
     * @return VerificationResponse
     */
    @Transactional
    public ApiResponse verifyUserOtp(@RequestBody UserOtpVerifyDto userOtpVerifyDto) {
        Optional<Users> optionalUsers = usersService.findByUsername(userOtpVerifyDto.getUsername());
        if (optionalUsers.isEmpty()) {
            // "사용자가 존재하지 않습니다."
            return ApiResponse.fail("The user does not exist.");
        }
        Users users = optionalUsers.get();

        Optional<UserOtp> optionalUserOtp = userOtpRepository.findTopByUserIdOrderByCreatedAtDescLimit1(users.getUserId());
        if (optionalUserOtp.isEmpty()) {
            // "OTP가 설정되지 않았습니다. 먼저 OTP 코드를 설정해 주세요."
            return ApiResponse.fail("OTP has not been set up. Please set up an OTP code first.");
        }
        UserOtp userOtp = optionalUserOtp.get();

        // 이미 사용됨
        if ("Y".equals(userOtp.getUsedYn())) {
            return ApiResponse.fail(
                    // "이 OTP 코드는 이미 사용되었습니다."
                    "This OTP code has already been used.");
        }

        // 만료
        if (userOtp.getExpiredAt().isBefore(LocalDateTime.now())) {
            return ApiResponse.fail(
                    // "OTP 코드가 이미 만료되었습니다."
                    "The OTP code has already expired.");
        }

        // 실패 횟수 제한
        if (userOtp.getFailCount() >= userOtpFailCount) {
            return ApiResponse.fail(
                    // "OTP 실패 횟수가 초과되었습니다."
                    "OTP failure count exceeded.");
        }

        // OTP 불일치
        if (!userOtp.getOtpCode().equals(userOtpVerifyDto.getOtpCode())) {
            userOtp.setFailCount(userOtp.getFailCount() + 1);
            return ApiResponse.fail(
                    // "입력하신 OTP 코드가 일치하지 않습니다."
                    "The entered OTP code does not match.");
        }
        // 성공 처리 "OTP 코드가 성공적으로 활성화되었습니다."
        return ApiResponse.success(true, "OTP code has been successfully activated.");
    }
}