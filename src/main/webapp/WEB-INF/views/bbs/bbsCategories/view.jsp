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
                    <tr><td>ID</td><td>${bbsCategories.categoryId}</td></tr>
                    <tr><td>카테고리명</td><td>${bbsCategories.categoryName}</td></tr>
                    <tr><td>카테고리 코드</td><td>${bbsCategories.categoryCode}</td></tr>
                    <tr><td>카테고리 설명</td><td>${bbsCategories.description}</td></tr>
                    <tr><td>정렬 순서</td><td>${bbsCategories.sortOrder}</td></tr>
                    <tr><td>활성 여부</td><td>${bbsCategories.isActive}</td></tr>
                    <tr><td>익명 게시 허용</td><td>${bbsCategories.allowAnonymous}</td></tr>
                    <tr><td>파일 업로드 허용</td><td>${bbsCategories.allowFileUpload}</td></tr>
                    <tr><td>최대 파일 개수</td><td>${bbsCategories.maxFileCount}</td></tr>
                    <tr><td>읽기 권한</td><td>${bbsCategories.readPermission}</td></tr>
                    <tr><td>쓰기 권한</td><td>${bbsCategories.writePermission}</td></tr>
                    <tr><td>생성일</td><td>${bbsCategories.createdDate}</td></tr>
                    <tr><td>수정일</td><td>${bbsCategories.updatedDate}</td></tr>
                    <tr><td>생성자 ID</td><td>${bbsCategories.createdBy}</td></tr>
                </tbody>
            </table>
        </div>
    </div>
</main>
<script type="text/javascript">
    const MENU = "bbsCategories";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {

        console.log("view init");
    }


</script>