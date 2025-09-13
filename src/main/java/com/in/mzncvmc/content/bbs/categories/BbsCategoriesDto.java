package com.in.mzncvmc.content.bbs.categories;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BbsCategoriesDto {
    private Long categoryId; // 카테고리 ID
    private String categoryName; // 카테고리명
    private String categoryCode; // 카테고리 코드
    private String description; // 카테고리 설명
    private Integer sortOrder; // 정렬 순서
    private Boolean isActive; // 활성 여부
    private Boolean allowAnonymous; // 익명 게시 허용
    private Boolean allowFileUpload; // 파일 업로드 허용
    private Integer maxFileCount; // 최대 파일 개수
    private String readPermission; // 읽기 권한 (ALL, USER, ADMIN)
    private String writePermission; // 쓰기 권한 (USER, ADMIN)
    private LocalDateTime registrationDate; // 생성일
    private LocalDateTime updatedDate; // 수정일
    private Long createdBy; // 생성자 ID
}
