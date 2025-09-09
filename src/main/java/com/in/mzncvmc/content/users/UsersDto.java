package com.in.mzncvmc.content.users;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsersDto {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String status;

    // company 정보 포함
    private Long companyId;
    private String companyName;
    private String companyType;

    public UsersDto() {

    }
}
