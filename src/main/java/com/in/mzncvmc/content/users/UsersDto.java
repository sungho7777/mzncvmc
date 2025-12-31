package com.in.mzncvmc.content.users;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private Long userId; // 사용자 고유 ID
    private Long companyId; // 소속 회사 고유 ID (FK: company.company_id)
    private String companyName; // 소속 회사명
    private String companyType; // 소속 회사 형태(예: 주식회사, LLC 등)
    private String username; // 로그인 아이디
    private String fullName; // 사용자 이름
    private String email; // 이메일
    private String phone; // 전화번호
    private String role; // 권한
    private String status; // 계정 상태
    private String connected; // 접속여부
    private String pwNotifyDuration; // 비밀번호 알림기간

/*
userId
companyId
companyName
companyType
username
fullName
email
phone
role
status
connected
pwNotifyDuration
 */
}
