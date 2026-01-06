package com.in.mzncvmc.content.userMfa;

import com.in.mzncvmc.common.auth.service.MfaService;
import com.in.mzncvmc.content.mail.MailService;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersDto;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserMfaService {
    @Autowired
    private UserMfaRepository userMfaRepository;
    @Autowired
    private MfaService mfaService;
    @Autowired
    private MailService mailService;

    /**
     * R.데이터 목록조회
     *
     * @param search 조회할 목록 데이터 search
     * @return DataList 조회된 목록 데이터
     *
     */
    @Transactional(readOnly = true)
    public Page<UserMfaDto> getPagedLists(int page, int size, String search, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userId"));

        // status 처리
        Users.Status statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = Users.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // 유효하지 않은 status는 null로 처리
                statusEnum = null;
            }
        }

        Page<UserMfa> DataPage;

        if (search == null || search.isBlank()) {
            DataPage = userMfaRepository.findAll(pageable);
        } else {
            // 검색어 존재
            DataPage = userMfaRepository.searchAll(search.trim(), pageable);
        }

        return DataPage.map(this::entityToDto);
    }

    /**
     * D.데이터 삭제
     *
     * @param id 삭제할 데이터 ID
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void delete(Long userId) {
        userMfaRepository.findByUserId(userId).ifPresent(userMfa -> {
            userMfa.setMfaEnabled(false);
            userMfa.setMfaVerified(false);
            userMfa.setMfaSecret(null);
            userMfaRepository.save(userMfa);
        });
    }

    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private UserMfaDto entityToDto(UserMfa entity) {
        UserMfaDto dto = UserMfaDto.builder()
                .userId(entity.getUserId())
                .mfaEnabled("mfaEnabled")
                .mfaSecret(entity.getMfaSecret())
                .mfaVerified("mfaVerified")

                .build();

        return dto;
    }

    /**
     * 사용자의 MFA 활성화 여부 확인
     */
    public boolean isMfaEnabled(Long userId) {
        return userMfaRepository.findByUserId(userId)
                .map(UserMfa::isMfaEnabled)
                .orElse(false);
    }

    /**
     * 사용자의 MFA 정보 조회
     */
    public Optional<UserMfa> getUserMfa(Long userId) {


        return userMfaRepository.findByUserId(userId);
    }

    /**
     * MFA Secret 생성 및 임시 저장 (아직 활성화 안됨)
     */
    @Transactional
    public String initiateAndStoreMFASecret(Long userId) throws Exception {
        String secret = "";

        Optional<UserMfa> existingMfa = userMfaRepository.findByUserId(userId);

        UserMfa userMfa;
        if (existingMfa.isPresent()) {
            userMfa = existingMfa.get();

            secret = userMfa.getMfaSecret();
            if (!StringUtils.hasText(secret)) {
                //
                secret = mfaService.generateSecret();
                String encryptedSecret = mfaService.encryptSecret(secret);
                userMfa.setMfaSecret(encryptedSecret);
            }
        } else {
            // new user
            userMfa = new UserMfa();
            userMfa.setUserId(userId);

            secret = mfaService.generateSecret();
            String encryptedSecret = mfaService.encryptSecret(secret);
            userMfa.setMfaSecret(encryptedSecret);
        }

        userMfa.setMfaEnabled(false);
        userMfa.setMfaVerified(false);

        userMfaRepository.save(userMfa);

        return secret; // 평문 반환 (QR 코드 생성용)
    }

    /**
     * TOTP 코드 검증 및 MFA 활성화
     */
    @Transactional
    public boolean verifyAndEnableMFA(Long userId, String mfaCode, String email) throws Exception {
        // user_mfa 테이블에서 조회 (없으면 예외 발생)
        UserMfa userMfa = userMfaRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("MFA setup not initiated. Please scan QR code first."));

        // Secret이 없으면 예외 발생
        if (userMfa.getMfaSecret() == null || userMfa.getMfaSecret().isEmpty()) {
            throw new RuntimeException("MFA secret not generated. Please restart setup process.");
        }

        // 이미 활성화되어 있으면 중복 설정 방지
        if (userMfa.isMfaEnabled() && userMfa.isMfaVerified()) {
            throw new RuntimeException("MFA is already enabled for this user.");
        }
        // 확인 방법 1: 서버 시간 로깅
        System.out.println("Server time: " + new Date());
        System.out.println("Server timestamp: " + System.currentTimeMillis() / 1000);

        // 확인 방법 2: TOTP 라이브러리 내부 시간 확인
        TimeProvider timeProvider = new SystemTimeProvider();
        System.out.println("TOTP time: " + timeProvider.getTime());

        // Secret 복호화 및 TOTP 검증
        String decryptedSecret = mfaService.decryptSecret(userMfa.getMfaSecret());
        log.info("userId : " + userId);
        log.info("getMfaSecret : " + userMfa.getMfaSecret());
        log.info("decryptedSecret : " + decryptedSecret);
        log.info("mfaCode : " + mfaCode);
        boolean isValid = mfaService.verifyCode(decryptedSecret, mfaCode);

        if (isValid) {
            // MFA 활성화
            userMfa.setMfaEnabled(true);
            userMfa.setMfaVerified(true);
            userMfa.setLastVerifiedAt(LocalDateTime.now());
            userMfaRepository.save(userMfa);

            // 확인 이메일 발송
            sendMFAConfirmationEmail(email);
            return true;
        }

        return false;
    }

    /**
     * 로그인 시 TOTP 검증
     */
    public boolean verifyMFALogin(Long userId, String totpCode) throws Exception {
        UserMfa userMfa = userMfaRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("MFA not found for this user"));

        if (!userMfa.isMfaEnabled() || userMfa.getMfaSecret() == null) {
            throw new RuntimeException("MFA not enabled for this user");
        }

        String decryptedSecret = mfaService.decryptSecret(userMfa.getMfaSecret());
        boolean isValid = mfaService.verifyCode(decryptedSecret, totpCode);

        if (isValid) {
            userMfa.setLastVerifiedAt(LocalDateTime.now());
            userMfaRepository.save(userMfa);
        }

        return isValid;
    }


    /**
     * MFA 완전 삭제
     */
    @Transactional
    public void deleteMFA(Long userId) {
        userMfaRepository.deleteByUserId(userId);
    }

    /**
     * MFA 활성화 확인 이메일 발송
     */
    private void sendMFAConfirmationEmail(String email) {
        /*
        if (mailSender == null || email == null || email.isEmpty()) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("MFA Enabled Successfully");
            message.setText("Multi-Factor Authentication has been successfully enabled for your account.");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send MFA confirmation email: " + e.getMessage());
        }
        */
    }
}
