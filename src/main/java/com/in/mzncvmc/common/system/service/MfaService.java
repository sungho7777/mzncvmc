package com.in.mzncvmc.common.system.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Log4j2
@Service
public class MfaService {
    @Value("${spring.application.name}")
    private String appName;

    @Value("${user.mfa.encryption.key}")
    private String encryptionKey;

    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    /**
     * Base32 인코딩된 비밀 키 생성
     */
    public String generateSecret() {

        DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }

    /**
     * QR 코드 이미지를 Base64로 생성
     */
    public String generateQRCodeBase64(String username, String secret) throws Exception {
        String otpAuthUrl = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                appName, username, secret, appName
        );

        BitMatrix matrix = new MultiFormatWriter().encode(
                otpAuthUrl,
                BarcodeFormat.QR_CODE,
                300,
                300
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }

    /**
     * TOTP 코드 검증
     */
    public boolean verifyCode(String secret, String code) {


        return verifier.isValidCode(secret, code);
    }

    /**
     * 비밀 키 암호화 (DB 저장용)
     */
    public String encryptSecret(String secret) throws Exception {
        byte[] keyBytes = encryptionKey.substring(0, 16).getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encrypted = cipher.doFinal(secret.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 비밀 키 복호화 (검증용)
     */
    public String decryptSecret(String encryptedSecret) throws Exception {
        byte[] keyBytes = encryptionKey.substring(0, 16).getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedSecret));
        return new String(decrypted);
    }
}
