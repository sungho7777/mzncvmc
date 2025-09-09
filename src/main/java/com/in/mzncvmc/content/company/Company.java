package com.in.mzncvmc.content.company;


import jakarta.persistence.*;
import lombok.*;

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
    private Long companyId; // company_id

    @Column(nullable = false, length = 200)
    private String companyName;

    @Column(length = 200)
    private String companyEngName;

    @Column(nullable = false, unique = true, length = 20)
    private String businessNumber;

    @Column(length = 100)
    private String ceoName;

    private LocalDate establishedDate;

    @Column(length = 50)
    private String companyType;

    @Column(length = 100)
    private String industry;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String fax;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String website;

    @Column(length = 10)
    private String postalCode;

    @Column(length = 300)
    private String address;

    @Column(length = 300)
    private String addressDetail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime registrationDate;
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist() {
        registrationDate = LocalDateTime.now();
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
