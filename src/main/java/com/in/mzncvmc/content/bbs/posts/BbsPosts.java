package com.in.mzncvmc.content.bbs.posts;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "bbs_posts",
        indexes = {
                @Index(name = "idx_category_status", columnList = "category_id, status"),
                @Index(name = "idx_author_id", columnList = "author_id"),
                @Index(name = "idx_created_date", columnList = "created_date DESC"),
                @Index(name = "idx_view_count", columnList = "view_count DESC"),
                @Index(name = "idx_is_notice", columnList = "is_notice, is_top_fixed"),
                @Index(name = "idx_parent_group", columnList = "parent_id, group_id, group_order"),
                @Index(name = "idx_status_created", columnList = "status, created_date DESC")
                // FULLTEXT 인덱스는 MySQL 전용 기능이므로 JPA에서 직접 지정 불가 → NativeQuery 필요
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbsPosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @Comment("고유 ID")
    private Long postId;

    @Column(name = "category_id", nullable = false)
    @Comment("카테고리 ID")
    private Long categoryId;

    @Column(nullable = false, length = 500)
    @Comment("제목")
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Comment("내용")
    private String bbsContent;

    @Column(name = "author_id")
    @Comment("작성자 ID (NULL이면 익명)")
    private Long authorId;

    @Column(name = "author_name", length = 100)
    @Comment("작성자명 (익명 또는 닉네임)")
    private String authorName;

    @Column(name = "author_ip", length = 45)
    @Comment("작성자 IP")
    private String authorIp;

    @Column(name = "password", length = 100)
    @Comment("익명 게시글 비밀번호 (암호화)")
    private String password;

    // 상태
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("상태")
    private BbsPosts.Status status = Status.ACTIVE;

    @Column(name = "is_notice")
    @Comment("공지사항 여부")
    private Boolean isNotice = false;

    @Column(name = "is_top_fixed")
    @Comment("상단 고정 여부")
    private Boolean isTopFixed = false;

    @Column(name = "is_secret")
    @Comment("비밀글 여부")
    private Boolean isSecret = false;

    // 조회/통계
    @Column(name = "view_count")
    @Comment("조회수")
    private Long viewCount = 0L;

    @Column(name = "like_count")
    @Comment("좋아요 수")
    private Long likeCount = 0L;

    @Column(name = "dislike_count")
    @Comment("싫어요 수")
    private Long dislikeCount = 0L;

    @Column(name = "comment_count")
    @Comment("댓글 수")
    private Long commentCount = 0L;

    @Column(name = "file_count")
    @Comment("첨부파일 수")
    private Integer fileCount = 0;

    // 답글 관련
    @Column(name = "parent_id")
    @Comment("부모 게시글 ID")
    private Long parentId;

    @Column
    @Comment("답글 깊이")
    private Integer depth = 0;

    @Column(name = "group_id")
    @Comment("답글 그룹 ID")
    private Long groupId;

    @Column(name = "group_order")
    @Comment("그룹내 순서")
    private Integer groupOrder = 0;

    // 메타 정보
    @Column(length = 500)
    @Comment("태그 (쉼표 구분)")
    private String tags;

    @Column(name = "meta_data", columnDefinition = "json")
    @Comment("추가 메타 데이터")
    private String metaData;

    // 시간 정보
    @Column(name = "created_date", updatable = false, insertable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("생성 일시")
    private LocalDateTime createdDate;

    @Column(name = "updated_date", insertable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정 일시")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    @Comment("삭제 일시")
    private LocalDateTime deletedDate;

    public enum Status {
        ACTIVE, HIDDEN, DELETED, BLOCKED
    }
}