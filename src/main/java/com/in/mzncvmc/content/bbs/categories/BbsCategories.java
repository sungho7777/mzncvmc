package com.in.mzncvmc.content.bbs.categories;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bbs_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbsCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("카테고리 ID")
    private Long categoryId;

    @Column(name = "category_name", nullable = false, length = 100)
    @Comment("카테고리명")
    private String categoryName;

    @Column(name = "category_code", nullable = false, length = 50, unique = true)
    @Comment("카테고리 코드")
    private String categoryCode;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("카테고리 설명")
    private String description;

    @Column(name = "sort_order")
    @Comment("정렬 순서")
    private Integer sortOrder;

    @Column(name = "is_active")
    @Comment("활성 여부")
    private Boolean isActive;

    @Column(name = "allow_anonymous")
    @Comment("익명 게시 허용")
    private Boolean allowAnonymous;

    @Column(name = "allow_file_upload")
    @Comment("파일 업로드 허용")
    private Boolean allowFileUpload;

    @Column(name = "max_file_count")
    @Comment("최대 파일 개수")
    private Integer maxFileCount;

    @Column(name = "read_permission", length = 50)
    @Comment("읽기 권한 (ALL, USER, ADMIN)")
    private String readPermission;

    @Column(name = "write_permission", length = 50)
    @Comment("쓰기 권한 (USER, ADMIN)")
    private String writePermission;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    @Comment("생성일")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    @Comment("수정일")
    private LocalDateTime updatedDate;

    @Column(name = "created_by")
    @Comment("생성자 ID")
    private Long createdBy;
}