package com.in.mzncvmc.content.bbs.posts;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BbsPostsDto {
    private Long postId;
    private Long categoryId;
    private String title;
    private String bbsContent;
    private Long authorId;
    private String authorName;
    private String authorIp;
    private String password;
    private String status;
    private Boolean isNotice;
    private Boolean isTopFixed;
    private Boolean isSecret;
    private Long viewCount;
    private Long likeCount;
    private Long dislikeCount;
    private Long commentCount;
    private Integer fileCount;
    private Long parentId;
    private Integer depth;
    private Long groupId;
    private Integer groupOrder;
    private String tags;
    private String metaData;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;

/*

26개
postId
categoryId
title
bbsContent
authorId
authorName
authorIp
password
status
isNotice
isTopFixed
isSecret
viewCount
likeCount
dislikeCount
commentCount
fileCount
parentId
depth
groupId
groupOrder
tags
metaData
createdDate
updatedDate
deletedDate

 */

}
