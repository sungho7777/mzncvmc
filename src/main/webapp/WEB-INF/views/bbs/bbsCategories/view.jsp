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
                    <tr><td>ID</td><td id="categoryId"></td></tr>
                    <tr><td>카테고리명</td><td id="categoryName"></td></tr>
                    <tr><td>카테고리 코드</td><td id="categoryCode"></td></tr>
                    <tr><td>카테고리 설명</td><td id="description"></td></tr>
                    <tr><td>정렬 순서</td><td id="sortOrder"></td></tr>
                    <tr><td>활성 여부</td><td id="isActive"></td></tr>
                    <tr><td>익명 게시 허용</td><td id="allowAnonymous"></td></tr>
                    <tr><td>파일 업로드 허용</td><td id="allowFileUpload"></td></tr>
                    <tr><td>최대 파일 개수</td><td id="maxFileCount"></td></tr>
                    <tr><td>읽기 권한</td><td id="readPermission"></td></tr>
                    <tr><td>쓰기 권한</td><td id="writePermission"></td></tr>
                    <tr><td>생성일</td><td id="createdDate"></td></tr>
                    <tr><td>수정일</td><td id="updatedDate"></td></tr>
                    <tr><td>생성자 ID</td><td id="createdBy"></td></tr>
                </tbody>
            </table>
            <button type="button" class="btn btn-primary" onclick="goList('bbs/bbsCategories');">목록</button>
            <button type="button" class="btn btn-warning" onclick="goAmend('bbs/bbsCategories', ${id}, 'PUT');">수정</button>
        </div>
    </div>
</main>
<script type="text/javascript">
    const ID = ${id};
    const MENU = "bbs/bbsCategories";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {

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

        $("#categoryId").text(data.categoryId);
        $("#categoryName").text(data.categoryName);
        $("#categoryCode").text(data.categoryCode);
        $("#description").text(data.description);
        $("#sortOrder").text(data.sortOrder);
        $("#isActive").text(data.isActive);
        $("#allowAnonymous").text(data.allowAnonymous);
        $("#allowFileUpload").text(data.allowFileUpload);
        $("#maxFileCount").text(data.maxFileCount);
        $("#readPermission").text(data.readPermission);
        $("#writePermission").text(data.writePermission);
        $("#createdDate").text(data.createdDate);
        $("#updatedDate").text(data.updatedDate);
        $("#createdBy").text(data.createdBy);
    };
</script>