package com.in.mzncvmc.common.system.files;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalFileExceptionHandler {

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Map<String, Object>> handleFileUploadException(FileUploadException e) {
        log.error("파일 업로드 예외: {}", e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("파일 크기 초과: {}", e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "파일 크기가 허용된 최대 크기를 초과했습니다.");

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 인수: {}", e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        log.error("예상하지 못한 오류: {}", e.getMessage(), e);

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "서버 내부 오류가 발생했습니다.");

        return ResponseEntity.internalServerError().body(response);
    }
}
