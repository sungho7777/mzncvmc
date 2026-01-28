package com.in.mzncvmc.common.system.files;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FileUploadService {

    private final FileRepository fileRepository;
    private final Tika tika = new Tika();

    @Value("${file.upload.base-path:/uploads}")
    private String basePath;

    @Value("${file.upload.max-file-size:52428800}")
    private long maxFileSize;

    @Value("${file.upload.allowed-extensions}")
    private String allowedExtensions;

    @Value("${file.upload.blocked-extensions}")
    private String blockedExtensions;

    /**
     * 단일 파일 업로드
     */
    public FileEntity uploadFile(MultipartFile file, String referenceType, Long referenceId, Long userId) {
        validateFile(file);

        try {
            String storedFilename = generateStoredFilename(file.getOriginalFilename());
            String relativePath = generateRelativePath(referenceType);
            Path fullPath = Paths.get(basePath, relativePath);

            // 디렉토리 생성
            Files.createDirectories(fullPath);

            // 파일 저장
            Path filePath = fullPath.resolve(storedFilename);
            Files.write(filePath, file.getBytes());

            // DB에 파일 정보 저장
            FileEntity fileEntity = FileEntity.builder()
                    .referenceType(referenceType)
                    .referenceId(referenceId)
                    .originalFilename(file.getOriginalFilename())
                    .storedFilename(storedFilename)
                    .filePath(Paths.get(relativePath, storedFilename).toString())
                    .fileSize(file.getSize())
                    .fileExtension(FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase())
                    .mimeType(tika.detect(file.getInputStream()))
                    .createdBy(userId)
                    .isDeleted(false)
                    .deletedDate(null)
                    .build();

            return fileRepository.save(fileEntity);

        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }
    /**
     * 다중 파일 업로드
     */
    public List<FileEntity> uploadFiles(MultipartFile[] files, String referenceType, Long referenceId, Long userId) {
        return Arrays.stream(files)
                .filter(file -> !file.isEmpty())
                .map(file -> uploadFile(file, referenceType, referenceId, userId))
                .toList();
    }

    /**
     * 파일 삭제 (논리 삭제)
     */
    public boolean deleteFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findActiveFileById(fileId);
        if (fileEntity == null) {
            return false;
        }

        fileEntity.setIsDeleted(true);
        fileEntity.setDeletedDate(LocalDateTime.now());
        fileRepository.save(fileEntity);

        // 실제 파일 삭제 (선택사항)
        try {
            Path filePath = Paths.get(basePath, fileEntity.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("실제 파일 삭제 실패: {}", e.getMessage());
        }

        return true;
    }

    /**
     * 참조 타입과 ID로 파일 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FileEntity> getFilesByReference(String referenceType, Long referenceId) {
        return fileRepository.findActiveFilesByReference(referenceType, referenceId);
    }

    /**
     * 파일 유효성 검증
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("파일 크기가 제한을 초과했습니다. (최대: " + (maxFileSize / 1024 / 1024) + "MB)");
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();

        // 차단된 확장자 검사
        List<String> blocked = Arrays.asList(blockedExtensions.split(","));
        if (blocked.contains(extension)) {
            throw new IllegalArgumentException("업로드가 금지된 파일 형식입니다: " + extension);
        }

        // 허용된 확장자 검사
        List<String> allowed = Arrays.asList(allowedExtensions.split(","));
        if (!allowed.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다: " + extension);
        }
    }

    /**
     * 저장될 파일명 생성 (UUID 기반)
     */
    private String generateStoredFilename(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 상대 경로 생성 (날짜 기반 폴더 구조)
     */
    private String generateRelativePath(String referenceType) {
        String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return referenceType + "/" + dateFolder;
    }


}
