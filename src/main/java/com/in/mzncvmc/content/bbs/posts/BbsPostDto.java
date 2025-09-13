package com.in.mzncvmc.content.bbs.posts;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BbsPostDto {
    private Long postId; // 게시글 ID
    private Long categoryId; // 카테고리 ID
    private String title; // 제목
    private String content; // 내용
    private String writer; // 작성자
    private String password; // 비밀번호 (익명 게시글용)
    private Integer viewCount; // 조회수
    private Boolean isNotice; // 공지사항 여부
    private Boolean isPrivate; // 비밀글 여부
    private Long createdBy; // 생성자 ID
    private LocalDateTime registrationDate; // 생성일
    private LocalDateTime updatedDate; // 수정일
}
