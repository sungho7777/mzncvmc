package com.in.mzncvmc.content.users.account;

import com.in.mzncvmc.common.system.mail.MailService;
import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.content.userMfa.UserMfaService;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountService {
    @Value("${user.first.password}")
    private String userFirstPassword;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final UserMfaService userMfaService;
    @Autowired
    private final MailService mailService;
/*
    @Autowired
    public AccountService(PasswordEncoder passwordEncoder, UsersRepository usersRepository, UserMfaService userMfaService, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.userMfaService = userMfaService;
        this.mailService = mailService;
    }
*/

    @Transactional
    public ApiResponse changePassword(String username, PasswordChangeDto request) {
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

        // 3. 새 비밀번호 암호화 후 저장
        String encoded = passwordEncoder.encode(request.getNewPassword());
        int updated = usersRepository.updatePassword(username, encoded);

        if (updated == 0) {
            // 비밀번호 변경 실패
            return ApiResponse.fail("Password change failed");
        }else{
            mailService.sendChangePassword(user.getEmail());

            // 사용자 mfa 정보도 초기화 한다.
            userMfaService.resetUserMfa(user.getUserId());
            mailService.sendResetUserMfa(user.getEmail());

            // 비밀번호 초기화 성공
            return ApiResponse.success(true,"Password Change successful");
        }
    }

    @Transactional
    public ApiResponse resetPassword(String username) {
        Optional<Users> optional = usersRepository.findByUsername(username);
        if (optional.isEmpty()) {
            // 해당 사용자를 찾을 수 없습니다.
            return ApiResponse.fail("User not found.");
        }
        // 0. 사용자 조회
        Users user = optional.get();

        // 1. 비밀번호 초기화 후 저장
        String encoded = passwordEncoder.encode(userFirstPassword);
        int updated = usersRepository.resetPassword(username, encoded);

        if (updated == 0) {
            // 비밀번호 초기화 실패
            return ApiResponse.fail("Password Reset Failed.");
        }else{
            mailService.sendResetPassword(user.getEmail());

            // 사용자 mfa 정보도 초기화 한다.
            userMfaService.resetUserMfa(user.getUserId());
            mailService.sendResetUserMfa(user.getEmail());

            // 비밀번호 초기화 성공
            return ApiResponse.success(true,"Password Reset Successful");
        }

    }
}
