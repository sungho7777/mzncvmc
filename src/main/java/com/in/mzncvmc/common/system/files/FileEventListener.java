package com.in.mzncvmc.common.system.files;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 파일 업로드 관련 이벤트 처리
 */
@Slf4j
@Component
public class FileEventListener {

    @EventListener
    public void handleFileUploadEvent(FileUploadEvent event) {
        FileEntity file = event.getFileEntity();
        log.info("파일 업로드 완료: {} ({})", file.getOriginalFilename(), file.getFileSize());

        // 필요한 후처리 작업
        // 예: 이미지 썸네일 생성, 바이러스 스캔, 로그 기록 등
        processUploadedFile(file);
    }

    @EventListener
    public void handleFileDeleteEvent(FileDeleteEvent event) {
        Long fileId = event.getFileId();
        log.info("파일 삭제 완료: {}", fileId);

        // 필요한 후처리 작업
        // 예: 캐시 삭제, 관련 데이터 정리 등
    }

    private void processUploadedFile(FileEntity file) {
        try {
            // 이미지 파일인 경우 썸네일 생성
            if (file.getMimeType() != null && file.getMimeType().startsWith("image/")) {
                // createThumbnail(file);
            }

            // 바이러스 스캔 (설정에서 활성화된 경우)
            // scanForVirus(file);

        } catch (Exception e) {
            log.error("파일 후처리 중 오류 발생: {}", e.getMessage());
        }
    }
}

/**
 * 파일 업로드 이벤트
 */
class FileUploadEvent {
    private final FileEntity fileEntity;

    public FileUploadEvent(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public FileEntity getFileEntity() {
        return fileEntity;
    }
}

/**
 * 파일 삭제 이벤트
 */
class FileDeleteEvent {
    private final Long fileId;

    public FileDeleteEvent(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileId() {
        return fileId;
    }
}
