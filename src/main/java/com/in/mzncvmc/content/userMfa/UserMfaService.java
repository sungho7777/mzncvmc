package com.in.mzncvmc.content.userMfa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.in.mzncvmc.common.system.mail.MailService;
import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.common.system.service.MfaService;
import com.in.mzncvmc.content.userMfa.backupCode.BackupCodeService;
import com.in.mzncvmc.content.users.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserMfaService {
    @Value("${user.mfa.fail.count}")
    private Integer userMfaFailCount;

    @Autowired
    private final UserMfaRepository userMfaRepository;
    @Autowired
    private final MfaService mfaService;
    @Autowired
    private final MailService mailService;
    @Autowired
    private final BackupCodeService backupCodeService;

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
     * R.데이터 단일조회
     *
     * @param id 조회할 단일 데이터 ID
     * @return DataDto 조회된 단일 데이터
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional(readOnly = true)
    public UserMfaDto findByUserId(Long id) {
        UserMfa userMfa = userMfaRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        return entityToDto(userMfa); // 엔티티 → DTO 변환 메서드
    }

    /**
     * U.데이터 초기화 처리한다.(-> 사용자는 다시 설정해야 함.)
     *
     * @param id 초기화 할 데이터 ID
     */
    @Transactional
    public void resetUserMfa(Long id) {
        userMfaRepository.findByUserId(id).ifPresent(userMfa -> {
            userMfa.setMfaEnabled(false);
            userMfa.setMfaVerified(false);
            userMfa.setMfaSecret(null);
            userMfa.setFailedAttempts(0); // 실패 횟수 초기화
            userMfa.setLockedUntil(null); // 잠금 해제 시간 초기화
            userMfa.setBackupCodes(null); // 백업코드 초긱화
            userMfaRepository.save(userMfa);
        });
    }
    /**
     * D.데이터 삭제
     *
     * @param id 삭제할 데이터 ID
     * @throws IllegalArgumentException 데이터 미존재
    @Transactional
    public void delete(Long id) {
        UserMfa userMfa = userMfaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        userMfaRepository.delete(userMfa);
    }
     */

    /**
     * D.MFA 완전 삭제
     *
     * @param userId 삭제할 데이터 Vo

    @Transactional
    public void deleteMFA(Long userId) {

        userMfaRepository.deleteByUserId(userId);
    }*/

    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private UserMfaDto entityToDto(UserMfa entity) {
        UserMfaDto dto = UserMfaDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .mfaEnabled(entity.isMfaEnabled())
                .mfaSecret(entity.getMfaSecret())
                .mfaVerified(entity.isMfaVerified())
                .failedAttempts(entity.getFailedAttempts())
                .backupCodes(entity.getBackupCodes())

                .build();

        return dto;
    }

    /**
     * 사용자의 MFA 활성화 여부 확인
     *  true → MFA 활성화
     *  false → MFA 비활성화 또는 설정 정보 없음
     * @param userId
     * @return boolean
     */
    public boolean isMfaEnabled(Long userId) {
        return userMfaRepository.findByUserId(userId)
                .map(UserMfa::isMfaEnabled)
                .orElse(false);
    }

    /**
     * 사용자의 MFA 정보 조회
     *
     * @param userId
     * @return UserMfa
    public Optional<UserMfa> getUserMfa(Long userId) {


        return userMfaRepository.findByUserId(userId);
    }
     */

    /**
     * MFA Secret 생성 및 임시 저장 (아직 활성화 안됨)
     * (MFA 비밀 키를 초기화하고 DB저장합니다.)
     *
     * @param userId
     * @return String secret
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
                // 초기화 된 유져
                secret = mfaService.generateSecret();
                String encryptedSecret = mfaService.encryptSecret(secret);
                userMfa.setMfaSecret(encryptedSecret);
            }
        } else {
            // 새로운 유져
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
     * TOTP 코드 검증 및 MFA 활성화 (MFA를 확인하고 활성화 한다.)
     *
     * @param userId, inputCode, email
     * @return VerificationResponse
     */
    @Transactional
    public ApiResponse verifyAndEnableMFA(Long userId, String inputCode, String email, String mapping) throws Exception {
        // user_mfa 테이블에서 조회 (없으면 예외 발생)
        Optional<UserMfa> optional = userMfaRepository.findByUserId(userId);
        if (optional.isEmpty()) {
            // "MFA 설정이 시작되지 않았습니다. 먼저 QR 코드를 스캔해 주세요."
            return ApiResponse.fail("MFA setup not initiated. Please scan QR code first.");
        }
        UserMfa userMfa = optional.get();

        // Secret이 없으면 예외 발생
        if (userMfa.getMfaSecret() == null || userMfa.getMfaSecret().isEmpty()) {
            // "MFA 비밀 키가 생성되지 않았습니다. 설정 프로세스를 다시 시작하십시오."
            return ApiResponse.fail("MFA secret not generated. Please restart setup process.");
        }

        // 이미 활성화되어 있으면 중복 설정 방지(초기설정때 체크)
        if (mapping.equals("generate") &&
                userMfa.isMfaEnabled() && userMfa.isMfaVerified()) {
            // "이 사용자에게는 이미 MFA가 활성화되어 있습니다."
            return ApiResponse.fail("MFA is already enabled for this user.");
        }

        // 잠금 상태 확인
        if (userMfa.getLockedUntil() != null && LocalDateTime.now().isBefore(userMfa.getLockedUntil())) {
            long minutesLeft = java.time.Duration.between(LocalDateTime.now(), userMfa.getLockedUntil()).toMinutes();
            // "로그인 시도 횟수 초과로 계정이 잠겼습니다. %d분 후에 다시 시도해 주세요."
            return ApiResponse.fail(String.format("Account locked due to too many failed attempts. Try again in %d minute(s).", minutesLeft + 1));
        }
        // 잠금 시간이 지났으면 초기화
        if (userMfa.getLockedUntil() != null && LocalDateTime.now().isAfter(userMfa.getLockedUntil())) {
            userMfa.setLockedUntil(null);
            userMfa.setFailedAttempts(0);
            userMfaRepository.save(userMfa);
        }

        // 실패 횟수 체크 (5회[userMfaFailCount] 제한)
        if (userMfa.getFailedAttempts() >= userMfaFailCount) {
            // 30분 잠금
            userMfa.setLockedUntil(LocalDateTime.now().plusMinutes(30));
            userMfaRepository.save(userMfa);

            mailService.sendUserMfaFailCountLock(email);

            // "로그인 시도 횟수가 너무 많습니다. 계정이 30분간 잠금 처리됩니다."
            return ApiResponse.fail("Too many failed attempts. Account locked for 30 minutes.");
        }

        // Secret 복호화 및 TOTP 검증
        String decryptedSecret = mfaService.decryptSecret(userMfa.getMfaSecret());
        boolean isValid = mfaService.verifyCode(decryptedSecret, inputCode);

        if (isValid) {
            // MFA 활성화
            userMfa.setMfaEnabled(true);
            userMfa.setMfaVerified(true);
            userMfa.setLastVerifiedAt(LocalDateTime.now());
            userMfa.setFailedAttempts(0);

            if(mapping.equals("generate")){
                // 구글 TOTP 코드 [초기 설정]을 위한 검증_generate
                // mapping = "generate"
                // 신규 사용자이거나, mfa 설정이 초기화 된 사용자만 백업키 설정 한다.
                // 백업 코드 생성
                List<String> backupCodes = backupCodeService.generateBackupCodes();
                // 백업 코드 해싱 후 JSON으로 저장
                List<Map<String, Object>> backupCodeList = new ArrayList<>();
                for (String code : backupCodes) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("code", backupCodeService.hashBackupCode(code));
                    entry.put("used", false);
                    backupCodeList.add(entry);
                }
                ObjectMapper mapper = new ObjectMapper();
                userMfa.setBackupCodes(mapper.writeValueAsString(backupCodeList));

                userMfaRepository.save(userMfa);
                // MFA 확인 이메일 발송
                mailService.sendMFAConfirmationEmail(email, backupCodes);

                // "MFA가 성공적으로 활성화되었습니다. 다시 로그인 시도하세요."
                return ApiResponse.success(true,"MFA has been successfully activated. Please log in again.");
            }else{
                // 구글 TOTP 코드 [로그인]을 위한 검증_verify
                // mapping = "verify"
                userMfaRepository.save(userMfa);

                // "구글 TOTP 인증이 성공하였습니다."
                return ApiResponse.success(true,"Google TOTP authentication was successful.");
            }
        } else {
            // 실패: 실패 횟수 증가
            userMfa.setFailedAttempts(userMfa.getFailedAttempts() + 1);
            userMfaRepository.save(userMfa);

            int remainingAttempts = userMfaFailCount - userMfa.getFailedAttempts();
            if (remainingAttempts > 0) {
                // "잘못된 TOTP 코드입니다. 계정 잠금 전 남은 시도 횟수는 %d회입니다."
                return ApiResponse.fail(String.format("Invalid TOTP code. %d attempt(s) remaining before account lock.", remainingAttempts));
            } else {
                // 5회[userMfaFailCount] 실패 -> 30분 잠금
                userMfa.setLockedUntil(LocalDateTime.now().plusMinutes(30));
                userMfaRepository.save(userMfa);

                mailService.sendUserMfaFailCountInterruption(email);

                // "인증 시도 횟수가 너무 많아 실패했습니다. MFA 설정이 일시 중단되었습니다. 관리자에게 문의하십시오."
                return ApiResponse.fail("Too many failed attempts have occurred. MFA setup has been suspended. Please contact your administrator.");
            }
        }
    }

    /**
     * TOTP 백업코드 검증
     *
     * @param userId, userId, inputCode
     * @return VerificationResponse
     */
    @Transactional
    public ApiResponse verifyBackupCode(Long userId, String inputCode, String email) throws Exception {
        // user_mfa 테이블에서 조회 (없으면 예외 발생)
        Optional<UserMfa> optional = userMfaRepository.findByUserId(userId);
        if (optional.isEmpty()) {
            // "MFA 설정이 시작되지 않았습니다. 먼저 QR 코드를 스캔해 주세요."
            return ApiResponse.fail("MFA setup not initiated. Please scan QR code first.");
        }
        UserMfa userMfa = optional.get();

        if (userMfa.getBackupCodes() == null) {
            // "MFA Backup Code가 존재하지 않습니다."
            return ApiResponse.fail("MFA Backup Code does not exist.");
        }

        // JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> backupCodeList = mapper.readValue(
                userMfa.getBackupCodes(),
                new TypeReference<List<Map<String, Object>>>() {}
        );

        // 각 백업 코드 검증
        for (Map<String, Object> entry : backupCodeList) {
            boolean used = (boolean) entry.get("used");
            String hashedCode = (String) entry.get("code");

            if (!used && backupCodeService.verifyBackupCode(inputCode, hashedCode)) {
                // 사용 처리
                entry.put("used", true);
                userMfa.setBackupCodes(mapper.writeValueAsString(backupCodeList));
                userMfa.setLastVerifiedAt(LocalDateTime.now());
                userMfaRepository.save(userMfa);
                // "MFA Backup Code 으로 로그인에 성공하였습니다."
                return ApiResponse.success(true,"You have successfully logged in using the MFA Backup Code.");
            }
        }

        // "MFA Backup Code 인증에 실패하였습니다."
        return ApiResponse.fail("MFA Backup Code authentication failed.");
    }
}
