package com.in.mzncvmc.content.users.password;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "password_history")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // JPA 기본 생성자 (필수)
    protected PasswordHistory() {}

    //
    public PasswordHistory(Long userId, String password) {
        this.userId = userId;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
}
