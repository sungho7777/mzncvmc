package com.in.mzncvmc.content.bbs.posts;

import com.in.mzncvmc.content.bbs.categories.BbsCategories;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bbs_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbsPosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("게시글 ID")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Comment("카테고리 ID (외래키)")
    private BbsCategories category;

    @Column(name = "title", nullable = false, length = 200)
    @Comment("제목")
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @Comment("내용")
    private String content;

    @Column(name = "writer", length = 50)
    @Comment("작성자")
    private String writer;

    @Column(name = "password", length = 100)
    @Comment("비밀번호 (익명 게시글용)")
    private String password;

    @Column(name = "view_count")
    @Comment("조회수")
    private Integer viewCount;

    @Column(name = "is_notice")
    @Comment("공지사항 여부")
    private Boolean isNotice;

    @Column(name = "is_private")
    @Comment("비밀글 여부")
    private Boolean isPrivate;

    @Column(name = "created_by")
    @Comment("생성자 ID")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    @Comment("생성일")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    @Comment("수정일")
    private LocalDateTime updatedDate;
}