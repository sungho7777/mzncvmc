package com.in.mzncvmc.common.files;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileDownloadService {

    private final FileRepository fileRepository;

    @Value("${file.upload.base-path:/uploads}")
    private String basePath;

    /**
     * 파일 다운로드
     */
    public ResponseEntity<Resource> downloadFile(Long fileId) {
        FileEntity fileEntity = fileRepository.findActiveFileById(fileId);
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = Paths.get(basePath).resolve(fileEntity.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                log.error("파일을 찾을 수 없거나 읽을 수 없습니다: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            String encodedFilename = URLEncoder.encode(fileEntity.getOriginalFilename(), "UTF-8")
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileEntity.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileEntity.getOriginalFilename() + "\"; filename*=UTF-8''" + encodedFilename)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileEntity.getFileSize()))
                    .body(resource);

        } catch (MalformedURLException | UnsupportedEncodingException e) {
            log.error("파일 다운로드 처리 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 파일 미리보기 (이미지 등)
     */
    public ResponseEntity<Resource> previewFile(Long fileId) {
        FileEntity fileEntity = fileRepository.findActiveFileById(fileId);
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }

        // 이미지 파일만 미리보기 허용
        if (!fileEntity.getMimeType().startsWith("image/")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Path filePath = Paths.get(basePath).resolve(fileEntity.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileEntity.getMimeType()))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(resource);

        } catch (MalformedURLException e) {
            log.error("파일 미리보기 처리 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
