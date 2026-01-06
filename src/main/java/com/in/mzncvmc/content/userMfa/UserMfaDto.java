package com.in.mzncvmc.content.userMfa;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMfaDto {
    private Long userId;
    private String mfaEnabled;
    private String mfaSecret;
    private String mfaVerified;
}
