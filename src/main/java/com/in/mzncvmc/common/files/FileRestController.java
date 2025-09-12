package com.in.mzncvmc.common.files;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileRestController {
    private final FileUploadService fileUploadService;
    private final FileDownloadService fileDownloadService;

    @Autowired
    public FileRestController(FileUploadService fileUploadService, FileDownloadService fileDownloadService) {
        this.fileUploadService = fileUploadService;
        this.fileDownloadService = fileDownloadService;
    }

    /**
     * 단일 파일 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("referenceType") String referenceType,
            @RequestParam("referenceId") Long referenceId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = getUserId(userDetails); // JWT에서 사용자 ID 추출
            FileEntity uploadedFile = fileUploadService.uploadFile(file, referenceType, referenceId, userId);

            response.put("success", true);
            response.put("message", "파일이 성공적으로 업로드되었습니다.");
            response.put("fileId", uploadedFile.getFileId());
            response.put("filename", uploadedFile.getOriginalFilename());
            response.put("fileSize", uploadedFile.getFileSize());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 다중 파일 업로드
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("referenceType") String referenceType,
            @RequestParam("referenceId") Long referenceId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = getUserId(userDetails);
            List<FileEntity> uploadedFiles = fileUploadService.uploadFiles(files, referenceType, referenceId, userId);

            response.put("success", true);
            response.put("message", uploadedFiles.size() + "개의 파일이 성공적으로 업로드되었습니다.");
            response.put("uploadedFiles", uploadedFiles.stream().map(file -> {
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("fileId", file.getFileId());
                fileInfo.put("filename", file.getOriginalFilename());
                fileInfo.put("fileSize", file.getFileSize());
                return fileInfo;
            }).toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 파일 목록 조회
     */
    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> getFiles(
            @RequestParam("referenceType") String referenceType,
            @RequestParam("referenceId") Long referenceId) {

        List<FileEntity> files = fileUploadService.getFilesByReference(referenceType, referenceId);
        return ResponseEntity.ok(files);
    }

    /**
     * 파일 다운로드
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        return fileDownloadService.downloadFile(fileId);
    }

    /**
     * 파일 미리보기
     */
    @GetMapping("/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable Long fileId) {
        return fileDownloadService.previewFile(fileId);
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = getUserId(userDetails);
            boolean deleted = fileUploadService.deleteFile(fileId, userId);

            if (deleted) {
                response.put("success", true);
                response.put("message", "파일이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "파일을 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "파일 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * JWT 토큰에서 사용자 ID 추출 (실제 구현에 맞게 수정 필요)
     */
    private Long getUserId(UserDetails userDetails) {
        // 실제 JWT 구현에 따라 수정 필요
        // 예: JwtUserDetails에서 userId 추출
        return 1L; // 임시 값
    }
}
