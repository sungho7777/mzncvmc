<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>

    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                <thead>
                    <tr>
                        <th style="width: 15%;">Entity</th>
                        <th>Content</th>
                    </tr>
                </thead>
                <tbody>

                    <tr><td>고유 ID</td><td id="postId"></td></tr>
                    <tr><td>카테고리 ID</td><td id="categoryId"></td></tr>
                    <tr><td>제목</td><td id="title"></td></tr>
                    <tr><td>내용</td><td id="bbsContent"></td></tr>
                    <tr><td>작성자 ID</td><td id="authorId"></td></tr>
                    <tr><td>작성자명</td><td id="authorName"></td></tr>
                    <tr><td>작성자 IP</td><td id="authorIp"></td></tr>
                    <tr><td>익명 게시글 비밀번호</td><td id="password"></td></tr>
                    <tr><td>상태</td><td id="status"></td></tr>
                    <tr><td>공지사항 여부</td><td id="isNotice"></td></tr>
                    <tr><td>상단 고정 여부</td><td id="isTopFixed"></td></tr>
                    <tr><td>비밀글 여부</td><td id="isSecret"></td></tr>
                    <tr><td>조회수</td><td id="viewCount"></td></tr>
                    <tr><td>좋아요 수</td><td id="likeCount"></td></tr>
                    <tr><td>싫어요 수</td><td id="dislikeCount"></td></tr>
                    <tr><td>댓글 수</td><td id="commentCount"></td></tr>
                    <tr><td>첨부파일 수</td><td id="fileCount"></td></tr>
                    <tr><td>부모 게시글 ID</td><td id="parentId"></td></tr>
                    <tr><td>답글 깊이</td><td id="depth"></td></tr>
                    <tr><td>답글 그룹 ID</td><td id="groupId"></td></tr>
                    <tr><td>그룹내 순서</td><td id="groupOrder"></td></tr>
                    <tr><td>태그</td><td id="tags"></td></tr>
                    <tr><td>추가 메타 데이터</td><td id="metaData"></td></tr>
                    <tr><td>생성 일시</td><td id="createdDate"></td></tr>
                    <tr><td>수정 일시</td><td id="updatedDate"></td></tr>
                    <tr><td>삭제 일시</td><td id="deletedDate"></td></tr>
                </tbody>
            </table>
            <button id="btnGoList" type="button" class="btn btn-primary" onclick="goList('bbs/bbsPosts', null);">목록</button>
            <button id="btnGoAmend" type="button" class="btn btn-warning" onclick="goAmend('bbs/bbsPosts', null, ${id}, 'PUT');">수정</button>
        </div>
    </div>
</main>
<script type="text/javascript">
    const ID = ${id};
    const CATEGORY_ID = ${categoryId};
    const MENU = "bbs/bbsPosts";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {
        if(!accessTokenCheck()) return false;


        getView();
        console.log("view init");
    }

    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getView = async() => {
        $('#loading').show();

        await fetch(API_URL + "/" + ID, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            }
        })
            .then(res => res.json())
            .then(result => {

                renderTable(result.data);
            })
            .finally(() => {
                setTimeout(() => $('#loading').hide(), 250);
            })
            .catch(err => console.error("에러:", err));
    };

    /**
     * ETC.그리드(테이블) 생성 함수
     * @param {number} data, tbodyId 데이터 ID
     * @returns {Grid} Grid
     */
    const renderTable = (data) => {

        $("#postId").text(data.postId);
        $("#categoryId").text(data.categoryId);
        $("#title").text(data.title);
        $("#bbsContent").html(data.bbsContent.replace(/\n/g, "<br>"));
        $("#authorId").text(data.authorId);
        $("#authorName").text(data.authorName);
        $("#authorIp").text(data.authorIp);
        $("#password").text(data.password);
        $("#status").text(data.status);
        $("#isNotice").text(data.isNotice);
        $("#isTopFixed").text(data.isTopFixed);
        $("#isSecret").text(data.isSecret);
        $("#viewCount").text(data.viewCount);
        $("#likeCount").text(data.likeCount);
        $("#dislikeCount").text(data.dislikeCount);
        $("#commentCount").text(data.commentCount);
        $("#fileCount").text(data.fileCount);
        $("#parentId").text(data.parentId);
        $("#depth").text(data.depth);
        $("#groupId").text(data.groupId);
        $("#groupOrder").text(data.groupOrder);
        $("#tags").text(data.tags);
        $("#metaData").text(data.metaData);
        $("#createdDate").text(data.createdDate);
        $("#updatedDate").text(data.updatedDate);
        $("#deletedDate").text(data.deletedDate);

        $("#btnGoList").attr("onclick", "goList('bbs/bbsPosts', "+data.categoryId+");");
        $("#btnGoAmend").attr("onclick", "goAmend('bbs/bbsPosts', "+data.categoryId+", " +data.postId+ ", 'PUT');");
    };
</script>