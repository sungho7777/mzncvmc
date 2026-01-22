package com.in.mzncvmc.content.userOtp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.HttpServletResponse;
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
     * 사용자 MFA QR 설치 이미지 생성
     *
     * @param response
     * @return ApiResponse
     */
    @Transactional
    public ApiResponse googleAuthenticatorQr(HttpServletResponse response) throws Exception {

        String qrText = "https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 250, 250);

        response.setContentType("image/png");
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", response.getOutputStream());


        return ApiResponse.success(true, "Qr successfully activated.");
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