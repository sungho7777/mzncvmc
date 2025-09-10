package com.in.mzncvmc.content.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CompanyDto {
    private Long companyId;
    private String companyName;
    private String companyEngName;
    private String businessNumber;
    private String ceoName;
    private LocalDate establishedDate;
    private String companyType;
    private String industry;
    private String phone;
    private String fax;
    private String email;
    private String website;
    private String postalCode;
    private String address;
    private String addressDetail;

    private String status;

    public CompanyDto(){

    }
}
