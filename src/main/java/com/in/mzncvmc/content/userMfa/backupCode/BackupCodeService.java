package com.in.mzncvmc.content.userMfa.backupCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupCodeService {
    @Value("${user.mfa.backup.characters.key}")
    private static final String CHARACTERS_KEY = "CHARACTERS_KEY";
    @Value("${user.mfa.backup.code.count}")
    private static final int CODE_COUNT = 10;
    @Value("${user.mfa.backup.code.length}")
    private static final int CODE_LENGTH = 12; // 4-4-4
    private final SecureRandom random = new SecureRandom();

    /**
     * 10개의 백업 코드 생성
     */
    public List<String> generateBackupCodes() {
        List<String> codes = new ArrayList<>();

        for (int i = 0; i < CODE_COUNT; i++) {
            StringBuilder code = new StringBuilder();

            // 12자리 생성 (4-4-4 형식)
            for (int j = 0; j < CODE_LENGTH; j++) {
                if (j > 0 && j % 4 == 0) {
                    code.append("-"); // 4자리마다 구분자
                }
                code.append(CHARACTERS_KEY.charAt(random.nextInt(CHARACTERS_KEY.length())));
            }

            codes.add(code.toString());
        }

        return codes;
    }

    /**
     * 백업 코드 해싱 (DB 저장용)
     */
    public String hashBackupCode(String code) {
        // BCrypt로 해싱 (TOTP Secret과 동일한 방식)
        return new BCryptPasswordEncoder().encode(code.replaceAll("-", ""));
    }

    /**
     * 백업 코드 검증
     */
    public boolean verifyBackupCode(String inputCode, String hashedCode) {
        String cleanInput = inputCode.replaceAll("-", "").toUpperCase();
        return new BCryptPasswordEncoder().matches(cleanInput, hashedCode);
    }
}
