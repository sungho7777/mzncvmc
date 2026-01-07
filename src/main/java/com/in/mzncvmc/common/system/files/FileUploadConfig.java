package com.in.mzncvmc.common.system.files;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
@Data
@Getter
@Setter
public class FileUploadConfig {
    private String basePath = "/uploads";
    private long maxFileSize = 52428800L; // 50MB
    private List<String> allowedExtensions;
    private List<String> blockedExtensions;
    private boolean virusScanEnabled = false;
    private boolean thumbnailGeneration = true;
    private int thumbnailWidth = 200;
    private int thumbnailHeight = 200;

}
