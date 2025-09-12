package com.in.mzncvmc.common.files;

/**
 * 파일 업로드 관련 예외 클래스들
 */
public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

class FileSizeExceededException extends FileUploadException {
    public FileSizeExceededException(String message) {
        super(message);
    }
}

class InvalidFileTypeException extends FileUploadException {
    public InvalidFileTypeException(String message) {
        super(message);
    }
}

class FileStorageException extends FileUploadException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

class FileNotFoundException extends FileUploadException {
    public FileNotFoundException(String message) {
        super(message);
    }
}