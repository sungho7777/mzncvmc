package com.in.mzncvmc.content.users.password;

import com.in.mzncvmc.common.system.mail.MailService;
import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.content.userMfa.UserMfaService;
import com.in.mzncvmc.content.userOtp.UserOtpService;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class PasswordService {
    @Value("${user.first.password}")
    private String userFirstPassword;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final PasswordHistoryRepository passwordHistoryRepository;
    @Autowired
    private final UserMfaService userMfaService;
    @Autowired
    private UserOtpService userOtpService;
    @Autowired
    private final MailService mailService;

    /**
     * 사용자 비밀번호 변경
     *
     * @param username 사용자 이름
     *        PasswordChangeDto
     * @return ApiResponse
     */
    @Transactional
    public ApiResponse change(String username, PasswordChangeDto request) {
        Optional<Users> optional = usersRepository.findByUsername(username);
        if (optional.isEmpty()) {
            // 해당 사용자를 찾을 수 없습니다.
            return ApiResponse.fail("User not found.");
        }
        // 0. 사용자 조회
        Users user = optional.get();

        // 1. 현재 비밀번호 검증
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            // 현재 비밀번호가 올바르지 않습니다.
            return ApiResponse.fail("Your current password is incorrect.");
        }

        // 2. 새 비밀번호 확인
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            // 새 비밀번호가 일치하지 않습니다.
            return ApiResponse.fail("The new passwords do not match.");
        }

        // 3. 직전 비밀번호 재사용 방지
        Optional<PasswordHistory> lastPasswordOpt =
                passwordHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(user.getUserId());

        if (lastPasswordOpt.isPresent()) {
            PasswordHistory lastPassword = lastPasswordOpt.get();

            // 새 비밀번호가 직전 비밀번호와 같은지 체크
            if (passwordEncoder.matches(request.getNewPassword(), lastPassword.getPassword())) {
                return ApiResponse.fail("You cannot reuse your previous password.");
            }
        }

        // 4. 최근 변경 5건 재사용 방지
        List<PasswordHistory> histories =
                passwordHistoryRepository.findTop5ByUserIdOrderByCreatedAtDesc(user.getUserId());

        for (PasswordHistory history : histories) {
            if (passwordEncoder.matches(request.getNewPassword(), history.getPassword())) {
                return ApiResponse.fail("You cannot reuse one of your recent passwords.");
            }
        }

        // 5. 현재 비밀번호를 history에 저장
        passwordHistoryRepository.save(
                new PasswordHistory(user.getUserId(), user.getPassword())
        );

        // 6. 새 비밀번호 암호화 후 저장
        String encoded = passwordEncoder.encode(request.getNewPassword());
        int updated = usersRepository.updatePassword(username, encoded);

        if (updated == 0) {
            // 비밀번호 변경 실패
            return ApiResponse.fail("Password change failed");
        }else{
            mailService.sendPasswordChange(user.getEmail());

            // 사용자 mfa 정보도 초기화 한다.
            userMfaService.resetUserMfa(user.getUserId());
            mailService.sendResetUserMfa(user.getEmail());

            // 비밀번호 초기화 성공
            return ApiResponse.success(true,"Password Change successful");
        }
    }

    /**
     * 사용자 이메일 복구 코드 전송 후 저장.
     *
     * @param passwordRecoveryDto
     * @return ApiResponse
     */
    public ApiResponse<?> recovery(@Valid PasswordRecoveryDto passwordRecoveryDto) {
        Optional<Users> optional = usersRepository.findByUsername(passwordRecoveryDto.getUsername());
        if (optional.isEmpty()) {
            // 해당 사용자를 찾을 수 없습니다.
            return ApiResponse.fail("User not found.");
        }
        // 0. 사용자 조회
        Users user = optional.get();

        // 1. 복구 코드 설정 후 저장(메일 OTP 사용함)
        String recoveryCode = userOtpService.generateUserOtp();
        userOtpService.createUserOtp(user.getUserId(), recoveryCode);

        // 이메일 발송
        mailService.sendUserOtpMail(user.getEmail(), recoveryCode);

        // "입력하신 이메일으로 비밀번호 복구 코드를 보냈습니다."
        return ApiResponse.success(true,"A password recovery code has been sent to the email address you entered.");
    }

    /**
     * 사용자 비밀번호 초기화.
     *
     * @param passwordRecoveryDto
     * @return ApiResponse
     */
    @Transactional
    public ApiResponse reset(@Valid PasswordRecoveryDto passwordRecoveryDto) {
        Optional<Users> optional = usersRepository.findByUsername(passwordRecoveryDto.getUsername());
        if (optional.isEmpty()) {
            // 해당 사용자를 찾을 수 없습니다.
            return ApiResponse.fail("User not found.");
        }
        // 0. 사용자 조회
        Users user = optional.get();

        // 1. 비밀번호 초기화 후 저장
        String encoded = passwordEncoder.encode(userFirstPassword);
        int updated = usersRepository.resetPassword(passwordRecoveryDto.getUsername(), encoded);

        if (updated == 0) {
            // 비밀번호 초기화 실패
            return ApiResponse.fail("Password Reset Failed.");
        }else{
            mailService.sendPasswordReset(user.getEmail());

            // 사용자 mfa 정보도 초기화 한다.
            userMfaService.resetUserMfa(user.getUserId());
            mailService.sendResetUserMfa(user.getEmail());

            // 비밀번호 초기화 성공
            return ApiResponse.success(true,"Password Reset Successful");
        }
    }
}
