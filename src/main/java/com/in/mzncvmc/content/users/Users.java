package com.in.mzncvmc.content.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in.mzncvmc.content.company.Company;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 고유 ID")
    private Long userId; // user_id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    @Comment("소속 회사 ID (FK: company.company_id)")
    private Company companyId;

    @Column(nullable = false, unique = true, length = 100)
    @Comment("로그인 아이디")
    private String username;

    @Column(nullable = false, length = 255)
    @Comment("암호화된 비밀번호")
    private String password;

    @Column(length = 100)
    @Comment("사용자 이름")
    private String fullName;

    @Column(length = 100, nullable = false, unique = true)
    @Comment("이메일")
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("provider")
    private Provider provider = Provider.LOCAL;

    @Column(name = "provider_id", length = 255, nullable = false)
    @Comment("provider Id")
    private String providerId;

    @Column(length = 20)
    @Comment("전화번호")
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("권한")
    private Role role = Role.USER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("계정 상태")
    private Status status = Status.ACTIVE;

    @Column(length = 3)
    @Comment("비밀번호 알림기간")
    private String pwNotifyDuration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("접속여부")
    private Connected connected = Connected.N;

    @Comment("등록일시")
    private LocalDateTime createdDate;
    @Comment("수정일시")
    private LocalDateTime updateDate;
    @Comment("마지막 로그인 시각")
    private LocalDateTime lastLogin;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }

    public enum Role {
        ADMIN,
        MANAGER,
        USER
    }

    public enum Provider {
        LOCAL, GOOGLE, KAKAO, NAVER
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    public enum Connected {
        N,  // Not Connected
        Y   // Connected
    }

    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    // UserDetails 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
