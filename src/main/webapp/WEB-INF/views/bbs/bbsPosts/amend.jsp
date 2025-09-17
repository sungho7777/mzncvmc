<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<main>
    <form id="amendForm">
        <input type="text" name="mapping" value="${mapping}">
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


                    <tr><td>ID</td><td><input type="text" name="postId" id="postId" value="0" /></td></tr>
                    <tr><td>카테고리 ID</td><td><input type="text" name="categoryId" id="categoryId" /></td></tr>
                    <tr><td>제목</td><td><input type="text" name="title" id="title" /></td></tr>
                    <tr><td>내용</td><td><input type="text" name="bbsContent" id="bbsContent" /></td></tr>
                    <tr><td>작성자 ID</td><td><input type="text" name="authorId" id="authorId" /></td></tr>
                    <tr><td>작성자명</td><td><input type="text" name="authorName" id="authorName" /></td></tr>
                    <tr><td>작성자 IP</td><td><input type="text" name="authorIp" id="authorIp" /></td></tr>
                    <tr><td>익명 게시글 비밀번호</td><td><input type="text" name="password" id="password" /></td></tr>
                    <tr><td>상태</td><td><input type="text" name="status" id="status" /></td></tr>
                    <tr><td>공지사항 여부</td><td><input type="text" name="isNotice" id="isNotice" /></td></tr>
                    <tr><td>상단 고정 여부</td><td><input type="text" name="isTopFixed" id="isTopFixed" /></td></tr>
                    <tr><td>비밀글 여부</td><td><input type="text" name="isSecret" id="isSecret" /></td></tr>
                    <tr><td>조회수</td><td><input type="text" name="viewCount" id="viewCount" /></td></tr>
                    <tr><td>좋아요 수</td><td><input type="text" name="likeCount" id="likeCount" /></td></tr>
                    <tr><td>싫어요 수</td><td><input type="text" name="dislikeCount" id="dislikeCount" /></td></tr>
                    <tr><td>댓글 수</td><td><input type="text" name="commentCount" id="commentCount" /></td></tr>
                    <tr><td>첨부파일 수</td><td><input type="text" name="fileCount" id="fileCount" /></td></tr>
                    <tr><td>부모 게시글 ID</td><td><input type="text" name="parentId" id="parentId" /></td></tr>
                    <tr><td>답글 깊이</td><td><input type="text" name="depth" id="depth" /></td></tr>
                    <tr><td>답글 그룹 ID</td><td><input type="text" name="groupId" id="groupId" /></td></tr>
                    <tr><td>그룹내 순서</td><td><input type="text" name="groupOrder" id="groupOrder" /></td></tr>
                    <tr><td>태그</td><td><input type="text" name="tags" id="tags" /></td></tr>
                    <tr><td>추가 메타 데이터</td><td><input type="text" name="metaData" id="metaData" /></td></tr>
                    <tr><td>생성 일시</td><td><input type="text" name="createdDate" id="createdDate" /></td></tr>
                    <tr><td>수정 일시</td><td><input type="text" name="updatedDate" id="updatedDate" /></td></tr>
                    <tr><td>삭제 일시</td><td><input type="text" name="deletedDate" id="deletedDate" /></td></tr>


                    </tbody>
                </table>
                <button id="btnGoList" type="button" class="btn btn-primary" onclick="goList('bbs/bbsCategories', null);">목록</button>
                <button type="button" class="btn btn-info" onclick="amendData();">${mapping eq 'POST' ? 'Create' : 'Amend'}</button>
            </div>
        </div>

    </form>
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

        if(Number(ID) > 0) getAmend();
        console.log("amend init");
    };

    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getAmend = async() => {
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
        $("#postId").val(data.postId);
        $("#categoryId").val(data.categoryId);
        $("#title").val(data.title);
        $("#bbsContent").val(data.bbsContent);
        $("#authorId").val(data.authorId);
        $("#authorName").val(data.authorName);
        $("#authorIp").val(data.authorIp);
        $("#password").val(data.password);
        $("#status").val(data.status);
        $("#isNotice").val(data.isNotice);
        $("#isTopFixed").val(data.isTopFixed);
        $("#isSecret").val(data.isSecret);
        $("#viewCount").val(data.viewCount);
        $("#likeCount").val(data.likeCount);
        $("#dislikeCount").val(data.dislikeCount);
        $("#commentCount").val(data.commentCount);
        $("#fileCount").val(data.fileCount);
        $("#parentId").val(data.parentId);
        $("#depth").val(data.depth);
        $("#groupId").val(data.groupId);
        $("#groupOrder").val(data.groupOrder);
        $("#tags").val(data.tags);
        $("#metaData").val(data.metaData);
        $("#createdDate").val(data.createdDate);
        $("#updatedDate").val(data.updatedDate);
        $("#deletedDate").val(data.deletedDate);

        $("#btnGoList").attr("onclick", "goList('bbs/bbsPosts', "+data.categoryId+");");
    };

    /**
     * CU.데이터 생성&수정 (Create&Update)
     * @param {number} id 데이터 ID
     * @param {Object} Data 수정할 데이터
     * @returns {Promise<Object>} 수정된 데이터 정보
     */
    const sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

    const amendData = async () => {
        const form = document.getElementById("amendForm");
        const data = Object.fromEntries(new FormData(form).entries());
        const mappingType = data.mapping == "PUT" ? "UPDATE" :
                            data.mapping == "POST" ? "NEW" : null;

        const errors = validateData(form, data);
        if (errors.length > 0) {
            alert(errors.join("\n"));
            return;
        }

        $('#loading').show(); // 로딩 표시

        try {
            const res = await fetch(API_URL + `/` + data.postId, {
                method: data.mapping,
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
                },
                body: JSON.stringify(data)
            });

            if (!res.ok) throw new Error("서버 에러 발생: " + res.status);

            const jsonData = await res.json();
            console.log("응답 updateData JSON:", jsonData);

            // 모달 띄우기
            $('#successModal').modal('show');
            // 모달 안의 버튼에 id 저장
            $('#success-btn').data('id', jsonData.data);
            $('#success-btn').data('categoryId', CATEGORY_ID);
            $('#success-btn').data('menu', MENU);

            //alert(mappingType + " 완료");

            //goView(MENU, jsonData.data);
        } catch (err) {
            console.error("에러:", err);
            alert(mappingType + " 실패: " + err.message);
        } finally {
            await sleep(250); // 최소 0.25초 로딩 유지
            $('#loading').hide();
        }
    };

    /**
     * ETC.데이터 validate Data Check
     *
     * @returns {errors} 데이터 정보
     */
    const validateData = (form, data) => {
        const errors = [];

        if (!data.mapping || data.mapping.trim() === "") {
            errors.push("mapping 값을 입력하세요.");
        }

        if (!data.title || data.title.trim() === "") {
            errors.push("제목을 입력하세요.");
        }

        if (!data.bbsContent || data.bbsContent.trim() === "") {
            errors.push("내용을 입력하세요.");
        }

        return errors;
    };
</script>
