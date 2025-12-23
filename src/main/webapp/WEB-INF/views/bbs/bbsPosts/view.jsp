<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="page-header page-header-compact page-header-light border-bottom bg-white mb-4">
    <div class="container-fluid px-4">
        <div class="page-header-content">
            <div class="row align-items-center justify-content-between pt-3">
                <div class="col-auto mb-3">
                    <h1 class="page-header-title">
                        <div class="page-header-icon"><i data-feather="globe"></i></div>
                        Bbs Posts
                    </h1>
                </div>
                <div class="col-12 col-xl-auto mb-3">
                    <a id="btnGoList" class="btn btn-sm btn-light text-primary" href="#" onclick="goList('bbs/bbsPosts', null);">
                        <i class="me-1" data-feather="arrow-left"></i>
                        Back to All Bbs Posts
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<div class="container-fluid px-4">
    <input type="hidden" name="postId" id="postId"  />

    <div class="row gx-4">
        <div class="col-lg-10">
            <div class="card mb-4">
                <div class="card-header">Bbs Title</div>
                <div class="card-body">
                    <div id="title" class="bg-light p-4 small"></div>
                </div>
            </div>
            <div class="card card-header-actions mb-4">
                <div class="card-header">
                    Bbs Preview
                    <i class="text-muted" data-feather="info" data-bs-toggle="tooltip" data-bs-placement="left" title="The Category preview text shows below the post Category, and is the Category summary on blog pages."></i>
                </div>
                <div class="card-body">
                    <div id="bbsContent" class="bg-light p-4 small"></div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header">Category Setting</div>
                <div class="card-body">
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">작성자 ID</label>
                            <div id="authorId" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="small mb-1">작성자명</label>
                            <div id="authorName" class="bg-light p-4 small"></div>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">익명 게시글 비밀번호</label>
                            <div id="password" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="small mb-1">상태</label>
                            <div id="status" class="bg-light p-4 small"></div>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">공지사항 여부</label>
                            <div id="isNotice" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="small mb-1">상단 고정 여부</label>
                            <div id="isTopFixed" class="bg-light p-4 small"></div>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">비밀글 여부</label>
                            <div id="isSecret" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-2">
            <div class="card card-header-actions">
                <div class="card-header">
                    Publish
                    <i class="text-muted" data-feather="info" data-bs-toggle="tooltip" data-bs-placement="left" title="After submitting, your Category will be published once it is approved by a moderator."></i>
                </div>
                <div class="card-body">
                    <div class="d-grid">
                        <button id="btnGoAmend" class="fw-500 btn btn-warning" onclick="goAmend('bbs/bbsPosts', null, ${id}, 'PUT');">Amend</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>










<%--
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
--%>





<script type="text/javascript">
    const ID = ${id};
    const CATEGORY_ID = ${categoryId};
    const MENU = "bbs/bbsPosts";
    const API_URL = "/api/" + MENU;

    window.onload = function() {
        if(!accessTokenCheck()) return false;

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