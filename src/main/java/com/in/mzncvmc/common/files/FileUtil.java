package com.in.mzncvmc.common.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FileUtil {

    private static final List<String> EXECUTABLE_EXTENSIONS = Arrays.asList(
            "exe", "bat", "cmd", "com", "pif", "scr", "vbs", "js", "jar", "php", "asp", "jsp", "sh"
    );

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg"
    );

    private static final List<String> DOCUMENT_EXTENSIONS = Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "hwp", "txt"
    );

    /**
     * 파일 확장자 검증
     */
    public static boolean isValidExtension(String filename, List<String> allowedExtensions) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return allowedExtensions.contains(extension);
    }

    /**
     * 실행 파일 여부 검증
     */
    public static boolean isExecutableFile(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return EXECUTABLE_EXTENSIONS.contains(extension);
    }

    /**
     * 이미지 파일 여부 확인
     */
    public static boolean isImageFile(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return IMAGE_EXTENSIONS.contains(extension);
    }

    /**
     * 문서 파일 여부 확인
     */
    public static boolean isDocumentFile(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return DOCUMENT_EXTENSIONS.contains(extension);
    }

    /**
     * 파일 크기 포맷팅
     */
    public static String formatFileSize(long size) {
        if (size <= 0) return "0 B";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.1f %s",
                size / Math.pow(1024, digitGroups),
                units[digitGroups]);
    }

    /**
     * 안전한 파일명 생성 (특수문자 제거)
     */
    public static String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9가-힣._-]", "_");
    }

    /**
     * 파일 해시값 계산 (중복 검증용)
     */
    public static String calculateFileHash(MultipartFile file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(file.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다.", e);
        }
    }

    /**
     * 디렉토리 생성
     */
    public static boolean createDirectories(String path) {
        try {
            Path directoryPath = Paths.get(path);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            return true;
        } catch (IOException e) {
            log.error("디렉토리 생성 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 파일 존재 여부 확인
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 파일 삭제
     */
    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * MIME 타입별 아이콘 클래스 반환
     */
    public static String getFileIconClass(String mimeType) {
        if (mimeType == null) return "fa-file";

        if (mimeType.startsWith("image/")) return "fa-file-image";
        if (mimeType.startsWith("video/")) return "fa-file-video";
        if (mimeType.startsWith("audio/")) return "fa-file-audio";
        if (mimeType.contains("pdf")) return "fa-file-pdf";
        if (mimeType.contains("word") || mimeType.contains("document")) return "fa-file-word";
        if (mimeType.contains("excel") || mimeType.contains("spreadsheet")) return "fa-file-excel";
        if (mimeType.contains("powerpoint") || mimeType.contains("presentation")) return "fa-file-powerpoint";
        if (mimeType.contains("zip") || mimeType.contains("compressed")) return "fa-file-archive";
        if (mimeType.contains("text")) return "fa-file-alt";

        return "fa-file";
    }
}
