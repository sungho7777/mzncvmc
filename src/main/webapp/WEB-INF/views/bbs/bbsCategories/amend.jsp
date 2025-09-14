<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<main>
    <form id="amendForm">
        <input type="text" name="mapping" value="${mapping}">
        <input type="text" name="categoryId" value="${empty bbsCategories.categoryId ? 0 : bbsCategories.categoryId}">
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
                    <tr><td>카테고리명</td><td><input type="text" name="categoryName" value="${bbsCategories.categoryName}" /></td></tr>
                    <tr><td>카테고리 코드</td><td><input type="text" name="categoryCode" value="${bbsCategories.categoryCode}" /></td></tr>
                    <tr><td>카테고리 설명</td><td><input type="text" name="description" value="${bbsCategories.description}" /></td></tr>
                    <tr><td>정렬 순서</td><td><input type="text" name="sortOrder" value="${bbsCategories.sortOrder}" /></td></tr>
                    <tr><td>활성 여부</td><td><input type="text" name="isActive" value="${bbsCategories.isActive}" /></td></tr>
                    <tr><td>익명 게시 허용</td><td><input type="text" name="allowAnonymous" value="${bbsCategories.allowAnonymous}" /></td></tr>
                    <tr><td>파일 업로드 허용</td><td><input type="text" name="allowFileUpload" value="${bbsCategories.allowFileUpload}" /></td></tr>
                    <tr><td>최대 파일 개수</td><td><input type="text" name="maxFileCount" value="${bbsCategories.maxFileCount}" /></td></tr>
                    <tr><td>읽기 권한</td><td><input type="text" name="readPermission" value="${bbsCategories.readPermission}" /></td></tr>
                    <tr><td>쓰기 권한</td><td><input type="text" name="writePermission" value="${bbsCategories.writePermission}" /></td></tr>
                    <tr><td>생성일</td><td><input type="text" name="createdDate" value="${bbsCategories.createdDate}" /></td></tr>
                    <tr><td>수정일</td><td><input type="text" name="updatedDate" value="${bbsCategories.updatedDate}" /></td></tr>
                    <tr><td>생성자 ID</td><td><input type="text" name="createdBy" value="${bbsCategories.createdBy}" /></td></tr>

                    </tbody>
                </table>
            </div>
        </div>
        <button type="button" onclick="amendData();">${empty bbsCategories.categoryId ? 'Create' : 'Amend'}</button>

    </form>
</main>




<script type="text/javascript">
    const MENU = "bbs/bbsCategories";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };

    const init = () => {

        console.log("amend init");
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
            const res = await fetch(API_URL + `/` + data.categoryId, {
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

        if (!data.categoryName || data.categoryName.trim() === "") {
            errors.push("카테고리명을 입력하세요.");
        }

        return errors;
    };
</script>
