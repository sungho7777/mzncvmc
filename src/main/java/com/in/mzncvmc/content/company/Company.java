package com.in.mzncvmc.content.company;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회사 고유 ID")
    private Long companyId; // company_id

    @Column(nullable = false, length = 200)
    @Comment("회사명")
    private String companyName;

    @Column(length = 200)
    @Comment("영문 회사명")
    private String companyEngName;

    @Column(nullable = false, unique = true, length = 20)
    @Comment("사업자 등록번호")
    private String businessNumber;

    @Column(length = 100)
    @Comment("대표자명")
    private String ceoName;

    @Comment("설립일")
    private LocalDate establishedDate;

    @Column(length = 50)
    @Comment("회사 형태(예: 주식회사, LLC 등)")
    private String companyType;

    @Column(length = 100)
    @Comment("업종")
    private String industry;

    @Column(length = 20)
    @Comment("대표 전화번호")
    private String phone;

    @Column(length = 20)
    @Comment("팩스 번호")
    private String fax;

    @Column(length = 100)
    @Comment("대표 이메일")
    private String email;

    @Column(length = 200)
    @Comment("홈페이지")
    private String website;

    @Column(length = 10)
    @Comment("우편번호")
    private String postalCode;

    @Column(length = 300)
    @Comment("주소")
    private String address;

    @Column(length = 300)
    @Comment("상세 주소")
    private String addressDetail;

    @Comment("등록일시")
    private LocalDateTime createdDate;
    @Comment("수정일시")
    private LocalDateTime updateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("상태")
    private Status status = Status.ACTIVE;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
