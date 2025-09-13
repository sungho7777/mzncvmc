package com.in.mzncvmc.content.company;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long companyId; // 회사 고유 ID
    private String companyName; // 회사명
    private String companyEngName; // 영문 회사명
    private String businessNumber; // 사업자 등록번호
    private String ceoName; // 대표자명
    private LocalDate establishedDate; // 설립일
    private String companyType; // 회사 형태(예: 주식회사, LLC 등)
    private String industry; // 업종
    private String phone; // 대표 전화번호
    private String fax; // 팩스 번호
    private String email; // 대표 이메일
    private String website; // 홈페이지
    private String postalCode; // 우편번호
    private String address; // 주소
    private String addressDetail; // 상세 주소
    private String status; // 상태
}
